package com.example.swp.controller;

import com.example.swp.DTO.CreateClassBySlotDTO;
import com.example.swp.DTO.EditClassDTO;
import com.example.swp.DTO.SlotRequest;
import com.example.swp.Entity.ClassesEntity;
import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.ClassMemberRepository;
import com.example.swp.Repository.ClassesRepository;
import com.example.swp.Repository.IUserRepository;
import com.example.swp.Service.IClassesService;
import com.example.swp.Service.IScheduleService;
import com.example.swp.Service.impl.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClassesController {

    private final ClassesRepository classesRepo;
    private final IClassesService classesService;
    private final IUserRepository userRepository;
    private final ClassMemberRepository classMemberRepository;
    private final IScheduleService scheduleService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        CreateClassBySlotDTO dto = new CreateClassBySlotDTO();
        dto.setSlots(new ArrayList<>(List.of(new SlotRequest())));
        model.addAttribute("classDTO", dto);
        model.addAttribute("trainers", userRepository.findAllByRole(UserRole.TRAINER));
        model.addAttribute("slotNumbers", List.of(1,2,3,4,5,6));
        return "classes/createClass";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("classDTO") CreateClassBySlotDTO dto,
                         BindingResult br, Model model) {

        if (br.hasErrors()) {
            model.addAttribute("classDTO", dto);
            model.addAttribute("trainers", userRepository.findAllByRole(UserRole.TRAINER));
            model.addAttribute("slotNumbers", List.of(1,2,3,4,5,6));
            return "classes/createClass";        }
        try {
            ClassesEntity created = classesService.createClassBySlots(dto);
            return "redirect:/classes/" + created.getId();
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("trainers", userRepository.findAllByRole(UserRole.TRAINER));
            model.addAttribute("slotNumbers", List.of(1,2,3,4,5,6));
            return "classes/createClass";
        }
    }

    @GetMapping
    public String list(@RequestParam(required=false) String className,
                       @RequestParam(required=false) String trainerLast,
                       @RequestParam(required=false) String gender,
                       @RequestParam(required=false, defaultValue="all") String mode, // all/upcoming/finished
                       @RequestParam(defaultValue="createdAt") String sortBy,
                       @RequestParam(defaultValue="desc") String dir,
                       @RequestParam(defaultValue="0") int page,
                       @RequestParam(defaultValue="6") int size,
                       Model model) {
        Pageable p = PageRequest.of(page, size,
                "asc".equalsIgnoreCase(dir)? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending());
        Page<ClassesEntity> pageData = classesService.search(className, trainerLast, gender, mode, p);
        model.addAttribute("pageData", pageData);
        model.addAttribute("className", className);
        model.addAttribute("trainerLast", trainerLast);
        model.addAttribute("gender", gender);
        model.addAttribute("mode", mode);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("dir", dir);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        return "classes/list";
    }


    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('MANAGER')")
    public String editForm(@PathVariable Long id, Model model) {
        ClassesEntity clazz = classesRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));

        EditClassDTO dto = new EditClassDTO();
        dto.setName(clazz.getName());
        dto.setDescription(clazz.getDescription());
        dto.setCapacity(clazz.getCapacity());
        dto.getNewSlots().add(new SlotRequest());
        if (dto.getNewSlots() == null || dto.getNewSlots().isEmpty()) {
            dto.setNewSlots(new ArrayList<>());
            dto.getNewSlots().add(new SlotRequest());
        }

        model.addAttribute("clazz", clazz);
        model.addAttribute("dto", dto);
        model.addAttribute("slotNumbers", List.of(1,2,3,4,5,6));
        return "classes/edit";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('MANAGER')")
    public String editSubmit(@PathVariable Long id,
                             @Valid @ModelAttribute("dto") EditClassDTO dto,
                             BindingResult br,
                             Model model,
                             RedirectAttributes ra) {
        if (br.hasErrors()) {
            ClassesEntity clazz = classesRepo.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
            model.addAttribute("clazz", clazz);
            model.addAttribute("slotNumbers", List.of(1,2,3,4,5,6));
            return "classes/edit";
        }
        try {
            // 1) Cập nhật thông tin lớp
            ClassesEntity updated = classesService.updateBasicInfo(id, dto.getName(), dto.getDescription(), dto.getCapacity());

            // 2) Thêm các slot mới
            if (dto.getNewSlots() != null && !dto.getNewSlots().isEmpty()) {
                ClassesEntity clazz = classesRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Class not found"));
                UserEntity trainer = clazz.getTrainer();

                // Lấy các slot hiện có trong lớp (để chặn trùng ngay trong lớp)
                Set<String> existingPairs = clazz.getSchedules().stream()
                        .map(s -> s.getSlot().getSlotDate() + "#" + s.getSlot().getSlotNumber().name())
                        .collect(Collectors.toSet());

                for (SlotRequest r : dto.getNewSlots()) {
                    if (r.getDate() == null || r.getSlotNumber() == null) continue;

                    // Chặn trùng slot ngay trong lớp
                    String pair = r.getDate() + "#SLOT_" + r.getSlotNumber();
                    if (existingPairs.contains(pair)) {
                        continue; // đã có trong lớp, bỏ qua
                    }

                    // Tạo schedule: service này chặn trùng slot của trainer
                    ScheduleEntity s = scheduleService.createSchedule(trainer, r.getDate(), r.getSlotNumber());
                    clazz.getSchedules().add(s);
                    existingPairs.add(pair);
                }
                classesRepo.save(clazz);
            }

            ra.addFlashAttribute("message", "Cập nhật thành công");
            return "redirect:/classes/" + id;
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/classes/" + id + "/edit";
        }
    }

        // Delete (Manager)
        @PostMapping("/{id}/delete")
        public String delete (@PathVariable Long id, RedirectAttributes ra){
            classesService.deleteById(id);
            ra.addFlashAttribute("message", "Đã xóa lớp");
            return "redirect:/classes";
        }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @AuthenticationPrincipal CustomUserDetails principal,
                         Model model) {
        ClassesEntity clazz = classesRepo.findWithAllById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));

        boolean noFutureSchedules = clazz.getSchedules() == null
                || clazz.getSchedules().stream()
                .noneMatch(s -> s.getSlot() != null && s.getSlot().getSlotDate() != null
                        && !s.getSlot().getSlotDate().isBefore(LocalDate.now()));

        long currentMembers = classMemberRepository.countByClassEntity_Id(id);
        boolean isFull = clazz.getCapacity() != null && currentMembers >= clazz.getCapacity();

        Long currentUserId = principal != null ? principal.getUser().getId() : null;
        boolean isJoined = currentUserId != null && classMemberRepository.existsById_ClassIdAndId_UserId(id, currentUserId);

        model.addAttribute("clazz", clazz);
        model.addAttribute("schedules", clazz.getSchedules());
        model.addAttribute("trainer", clazz.getTrainer());
        model.addAttribute("isJoined", isJoined);
        model.addAttribute("isFull", isFull);
        model.addAttribute("currentMembers", currentMembers);
        model.addAttribute("noFutureSchedules", noFutureSchedules);
        return "classes/detail";
    }


    @PostMapping("/{id}/register")
        public String register (@PathVariable Long id,
                                @AuthenticationPrincipal CustomUserDetails principal,
                                RedirectAttributes ra){

        if (principal == null) {
            ra.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để đăng ký lớp");
            return "redirect:/auth/login";
        }

            Long userId = principal.getUser().getId();
            classesService.register(id, userId);
            return "redirect:/classes/" + id + "?joined";
        }

        @PostMapping("/{id}/unregister")
        public String unregister (@PathVariable Long id,
                                  @AuthenticationPrincipal CustomUserDetails principal,
                                  RedirectAttributes ra){

            if (principal == null) {
                ra.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
                return "redirect:/auth/login";
            }

            Long userId = principal.getUser().getId();
            classesService.unregister(id, userId);
            return "redirect:/classes/" + id + "?left";
        }
    }
