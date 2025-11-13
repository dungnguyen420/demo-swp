package com.example.swp.Controller;

//import com.example.swp.Entity.UserEntity;
import com.example.swp.DTO.UserDTO;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/create")
    public String showCreatePackageForm(Model model) {
        model.addAttribute("packageDTO", new PackageDTO());
        return "auth/create-package-form";
    }
    @PostMapping("/create")
    public String createPackage(
            @Valid @ModelAttribute("packageDTO") PackageDTO packageDTO,
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
    public String deleteUser(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa người dùng thành công!");
        return "redirect:/auth/dashBoard?tab=users";
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

            userService.updateUser(id,dto);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");
            return "redirect:/auth/dashBoard?tab=users";

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
    @GetMapping("/update/{id}")
    public String showUpdateUserForm(@PathVariable("id") Long id, Model model) {
        try {

            UserEntity user = userService.findById(id);
            if (user == null) {
                return "redirect:/auth/dashBoard?tab=users&error=UserNotFound";
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
            return "redirect:/auth/dashBoard?tab=users&error=" + e.getMessage();
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
            return "redirect:/auth/dashBoard?tab=packages&error=" + e.getMessage();
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
