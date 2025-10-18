package com.example.swp.Controller;

import com.example.swp.DTO.PackageDTO;
import com.example.swp.DTO.RegisterDTO;
import com.example.swp.Entity.PackageEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import com.example.swp.Service.IPackageService;
import com.example.swp.Service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("auth")
public class DashBoardController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPackageService packageService;


    @GetMapping("/dashBoard")
    public String showDashBoard(Model model,
                                @RequestParam(name = "userPage", defaultValue = "0") int userPage,
                                @RequestParam(name = "packagePage", defaultValue = "0") int packagePage,
                                @RequestParam(name = "tab", defaultValue = "packages") String activeTab,
                                @RequestParam(name = "keyword", required = false) String keyword) {


        int userSize = 2;
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


        System.out.println("📦 Số lượng gói tập trong trang này: " + packagesPage.getContent().size());
        packagesPage.getContent().forEach(p -> System.out.println("   - " + p.getName()));

        model.addAttribute("activeTab", activeTab);
        return "auth/dashBoard";
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa người dùng thành công!");
        return "redirect:/auth/dashBoard?tab=users";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute RegisterDTO dto,
                             RedirectAttributes redirectAttributes) {
        userService.updateUser(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin người dùng thành công!");
        return "redirect:/auth/dashBoard?tab=users";
    }

    @GetMapping("/api/users/{id}")
    @ResponseBody
    public UserEntity getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }


    @PostMapping("/create")
    public String createPackage(@Valid @ModelAttribute PackageDTO packageDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại!");
            return "redirect:/auth/dashBoard?tab=packages";
        }
        packageService.createPackage(packageDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo gói tập mới thành công!");
        return "redirect:/auth/dashBoard?tab=packages";
    }

    @PostMapping("/delete-package")
    public String deletePackage(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        packageService.deletePackage(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa gói tập thành công!");
        return "redirect:/auth/dashBoard?tab=packages";
    }

    @GetMapping("/api/package/{id}")
    @ResponseBody
    public PackageDTO getPackageById(@PathVariable Long id) {
        return packageService.findPackageById(id);
    }

    @PostMapping("/update-package/{id}")
    public String updatePackage(@PathVariable("id") Long id,
                                @ModelAttribute PackageDTO dto,
                                RedirectAttributes redirectAttributes) {
        packageService.updatePackage(dto, id);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");
        return "redirect:/auth/dashBoard?tab=packages";
    }
}
