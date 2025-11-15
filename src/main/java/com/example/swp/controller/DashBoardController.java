package com.example.swp.Controller;

import com.example.swp.DTO.*;
import com.example.swp.Entity.PackageEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.OrderItemRepository;
import com.example.swp.Service.IPackageService;
import com.example.swp.Service.IUserService;
import com.example.swp.Service.impl.DashBoardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("auth")
public class DashBoardController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPackageService packageService;

    @Autowired
    private DashBoardService dashboardService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/dashBoard")
    public String showDashBoard(Model model,
                                @RequestParam(name = "userPage", defaultValue = "0") int userPage,
                                @RequestParam(name = "packagePage", defaultValue = "0") int packagePage,
                                @RequestParam(name = "tab", defaultValue = "packages") String tab,
                                @RequestParam(name = "keyword", required = false) String keyword,
                                @RequestParam(name = "filter", required = false) String filter,
                                @RequestParam(name = "from", required = false) String from,
                                @RequestParam(name = "to", required = false) String to,
                                @RequestParam(name = "username", required = false) String username,
                                @RequestParam(name = "successMessage", required = false) String successMessage,
                                @RequestParam(name = "errorMessage", required = false) String errorMessage) {

        if (!model.containsAttribute("successMessage") &&
                successMessage != null && !successMessage.isBlank()) {
            model.addAttribute("successMessage", successMessage);
        }
        if (!model.containsAttribute("errorMessage") &&
                errorMessage != null && !errorMessage.isBlank()) {
            model.addAttribute("errorMessage", errorMessage);
        }

        int userSize = 1;
        Page<UserEntity> usersPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            usersPage = userService.searchUsers(keyword.trim(), PageRequest.of(userPage, userSize));
            model.addAttribute("keyword", keyword);
        } else {
            usersPage = userService.findByRole(UserRole.MEMBER, PageRequest.of(userPage, userSize));
        }
        model.addAttribute("usersPage", usersPage);

        int packageSize = 5;
        Page<PackageEntity> packagesPage = packageService.findAll(PageRequest.of(packagePage, packageSize));
        model.addAttribute("packagesPage", packagesPage);

        System.out.println(" Số lượng gói tập trong trang này: " + packagesPage.getContent().size());
        packagesPage.getContent().forEach(p -> System.out.println("   - " + p.getName()));

        if ("revenue".equals(tab)) {
            RevenueSummaryDTO summary = dashboardService.getRevenueSummary();
            model.addAttribute("revenueSummary", summary);

            List<DailyRevenueDTO> last7Days = dashboardService.getRevenueLast7Days();
            model.addAttribute("revenueLast7Days", last7Days);

            Long filteredRevenue = dashboardService.getRevenueWithFilters(filter, from, to, username);
            model.addAttribute("filteredRevenue", filteredRevenue);

            model.addAttribute("filter", filter);
            model.addAttribute("from", from);
            model.addAttribute("to", to);
            model.addAttribute("username", username);
        }

        model.addAttribute("activeTab", tab);
        return "auth/dashBoard";
    }

    @GetMapping("/create")
    public String showCreatePackageForm(Model model) {
        model.addAttribute("packageDTO", new PackageDTO());
        return "auth/create-package-form";
    }

    @PostMapping("/create")
    public String createPackage(@Valid @ModelAttribute("packageDTO") PackageDTO packageDTO,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "auth/create-package-form";
        }

        try {
            packageService.createPackage(packageDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo gói mới thành công!");
            return "redirect:/auth/dashBoard?tab=packages";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/create-package-form";
        }
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id,
                             RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);


        redirectAttributes.addAttribute("tab", "userPage");
        redirectAttributes.addAttribute("successMessage", "Đã xóa người dùng thành công!");
        return "redirect:/auth/dashBoard";
    }


    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("userUpdateDTO") UserDTO dto,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("userId", id);
            return "auth/update-user-form";
        }

        try {
            userService.updateUser(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");
            return "redirect:/auth/dashBoard?tab=userPage";
        } catch (RuntimeException e) {
            model.addAttribute("userId", id);
            model.addAttribute("userUpdateError", e.getMessage());
            return "auth/update-user-form";
        }
    }

    @GetMapping("/api/users/{id}")
    @ResponseBody
    public UserEntity getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }


    @PostMapping("/delete-package")
    public String deletePackage(@RequestParam("id") Long id,
                                RedirectAttributes redirectAttributes) {


        if (orderItemRepository.existsByPackageId(id)) {
            redirectAttributes.addAttribute("tab", "packages");
            redirectAttributes.addAttribute("errorMessage",
                    "Gói tập này đã được khách hàng mua, không thể xóa.");
            return "redirect:/auth/dashBoard";
        }

        try {
            packageService.deletePackage(id);
            redirectAttributes.addAttribute("tab", "packages");
            redirectAttributes.addAttribute("successMessage", "Xóa gói tập thành công!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("tab", "packages");
            redirectAttributes.addAttribute("errorMessage", "Có lỗi xảy ra khi xóa gói tập.");
        }

        return "redirect:/auth/dashBoard";
    }

    @GetMapping("/api/package/{id}")
    @ResponseBody
    public PackageDTO getPackageById(@PathVariable Long id) {
        return packageService.findPackageById(id);
    }

    @GetMapping("/update/{id}")
    public String showUpdateUserForm(@PathVariable("id") Long id, Model model) {
        try {
            UserEntity user = userService.findById(id);
            if (user == null) {
                return "redirect:/auth/dashBoard?tab=userPage&errorMessage=UserNotFound";
            }

            UserDTO dto = new UserDTO();
            dto.setUserName(user.getUserName());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());

            model.addAttribute("userUpdateDTO", dto);
            model.addAttribute("userId", id);

            return "auth/update-user-form";

        } catch (Exception e) {
            return "redirect:/auth/dashBoard?tab=userPage&errorMessage=" + e.getMessage();
        }
    }

    @GetMapping("/update-package/{id}")
    public String showUpdatePackageForm(@PathVariable("id") Long id, Model model) {
        try {
            PackageDTO dto = packageService.findPackageById(id);
            model.addAttribute("packageDTO", dto);
            model.addAttribute("packageId", id);
            return "auth/update-package-form";
        } catch (Exception e) {
            return "redirect:/auth/dashBoard?tab=packages&errorMessage=" + e.getMessage();
        }
    }

    @PostMapping("/update-package/{id}")
    public String updatePackage(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("packageDTO") PackageDTO dto,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("packageId", id);
            return "auth/update-package-form";
        }

        try {
            packageService.updatePackage(dto, id);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật gói thành công!");
            return "redirect:/auth/dashBoard?tab=packages";
        } catch (RuntimeException e) {
            model.addAttribute("packageId", id);
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/update-package-form";
        }
    }
}
