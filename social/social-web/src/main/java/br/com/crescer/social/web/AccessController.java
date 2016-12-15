package br.com.crescer.social.web;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import br.com.crescer.social.entity.Usuario;
import br.com.crescer.social.exception.RegraNegocioException;
import br.com.crescer.social.service.RankService;
import br.com.crescer.social.service.UsuarioService;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccessController {

    @Autowired
    UsuarioService service;

    @Autowired
    RankService rankService;

    @RequestMapping("/login")
    String login(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:home";
        }
        Usuario usuario = new Usuario();
        if (usuario.getAmigos() == null) {
            usuario.setAmigos(new ArrayList<Usuario>());
        }
        if (usuario.getConvites() == null) {
            usuario.setConvites(new ArrayList<Usuario>());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("ranks", rankService.findAll());
        return "login";
    }

    @RequestMapping("/logout")
    String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:login";
    }

    @RequestMapping(value = "/add", method = POST)
    synchronized String add(@Valid Usuario usuario, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fielderr", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/login";
        }
        try {
            service.add(usuario);
            redirectAttributes.addFlashAttribute("cadmsg", "Usuário cadastrado com sucesso.");
        } catch (RegraNegocioException e) {
            redirectAttributes.addFlashAttribute("fielderr", e.getMessage());
        }
        return "redirect:/login";
    }

}
