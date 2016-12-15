package br.com.crescer.social.web;

import br.com.crescer.social.entity.Post;
import br.com.crescer.social.entity.Rank;
import br.com.crescer.social.entity.Usuario;
import br.com.crescer.social.exception.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import br.com.crescer.social.service.UsuarioService;
import br.com.crescer.social.service.PostService;
import br.com.crescer.social.service.RankService;
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class HomeController {

    @Autowired
    UsuarioService servico;

    @Autowired
    PostService postServico;

    @Autowired
    RankService rankServico;

    @RequestMapping(value = {"/home", "/"})
    public String home(Model model, @AuthenticationPrincipal User user) {
        Usuario atual = servico.findByEmail(user.getUsername());
        List<Usuario> convites = IteratorUtils.toList(atual.getConvites().iterator());
        List<Post> posts = postServico.findAllPosts(atual);
        model.addAttribute("totalPosts", posts.size());
        model.addAttribute("posts", posts);
        model.addAttribute("sessao", atual);
        model.addAttribute("convitesPendentes", convites.size());
        return "home";
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String find() {
        return "find";
    }

    @RequestMapping(value = "/find/user", method = RequestMethod.GET)
    public String find(Model model, String token, @AuthenticationPrincipal User user, Pageable p) {
        Pageable pageable = new PageRequest(p.getPageNumber(), 5, p.getSort());
        Usuario atual = servico.findByEmail(user.getUsername());
        Page<Usuario> lista = servico.findByContentExcept(token, atual.getId(), pageable);
        if (lista.getTotalPages() == 0) {
            return "fragments :: no-content";
        } else {
            model.addAttribute("lista", lista);
            return "fragments :: usuarios";
        }
    }

    @RequestMapping(value = "/home/adduser/{id_usuario}")
    public String addUser(Model model, @PathVariable(value = "id_usuario") Long id, @AuthenticationPrincipal User user) {
        Usuario atual = servico.findByEmail(user.getUsername());
        try {
            String nomeAAdicionar = servico.addFriend(atual, id);
            model.addAttribute("cl", "is-success");
            model.addAttribute("msg", String.format("Convite de amizade enviado com sucesso a %s.", nomeAAdicionar));
        } catch (RegraNegocioException e) {
            model.addAttribute("cl", e.getCl());
            model.addAttribute("msg", e.getMessage());
        }
        return "mensagem";
    }

    @RequestMapping(value = "/invites", method = RequestMethod.GET)
    public String invites(Model model, @AuthenticationPrincipal User user) {
        Usuario atual = servico.findByEmail(user.getUsername());
        model.addAttribute("sessao", atual);
        return "invites";
    }

    @RequestMapping(value = "/invites/accept/{id_usuario}", method = RequestMethod.GET)
    public String acceptInvite(@PathVariable(value = "id_usuario") Long id, Model model, @AuthenticationPrincipal User user) {
        Usuario atual = servico.findByEmail(user.getUsername());
        try {
            String nomeUsuarioAAceitar = servico.acceptFriend(atual, id);
            model.addAttribute("cl", "is-success");
            model.addAttribute("msg", String.format("Você agora tem %s na sua lista de amigos.", nomeUsuarioAAceitar));
        } catch (RegraNegocioException e) {
            model.addAttribute("cl", e.getCl());
            model.addAttribute("msg", e.getMessage());
        }
        model.addAttribute("sessao", atual);
        return "mensagem";
    }

    @RequestMapping(value = "/invites/deny/{id_usuario}", method = RequestMethod.GET)
    public String denyInvite(@PathVariable(value = "id_usuario") Long id, Model model, @AuthenticationPrincipal User user) {
        Usuario atual = servico.findByEmail(user.getUsername());
        try {
            String nomeUsuarioARecusar = servico.denyFriend(atual, id);
            model.addAttribute("cl", "is-success");
            model.addAttribute("msg", String.format("%s foi removido da sua lista de solicitações.", nomeUsuarioARecusar));
        } catch (RegraNegocioException e) {
            model.addAttribute("cl", e.getCl());
            model.addAttribute("msg", e.getMessage());
        }
        model.addAttribute("sessao", atual);
        return "mensagem";
    }

    @RequestMapping(value = "/post/new", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity insertNewPost(@Valid Post post, BindingResult bindingResult, @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        } else {
            Usuario atual = servico.findByEmail(user.getUsername());
            post.setUsuario(atual);
            postServico.save(post);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/post/refresh", method = RequestMethod.GET)
    public String refreshPosts(Model model, @AuthenticationPrincipal User user) {
        Usuario atual = servico.findByEmail(user.getUsername());
        List<Post> posts = postServico.findAllPosts(atual);
        model.addAttribute("posts", posts);
        return "fragments :: listposts";
    }

    @RequestMapping(value = "/post/refresh/total", method = RequestMethod.GET)
    @ResponseBody
    public int totalPosts(Model model, @AuthenticationPrincipal User user) {
        Usuario atual = servico.findByEmail(user.getUsername());
        List<Post> posts = postServico.findAllPosts(atual);
        return posts.size();
    }

    @RequestMapping(value = "/user/rankup", method = RequestMethod.GET)
    public String rankUp(Model model, @AuthenticationPrincipal User user) {
        Usuario atual = servico.findByEmail(user.getUsername());
        try {
            Rank newRank = rankServico.rankUp(atual);
            model.addAttribute("cl", "is-success");
            model.addAttribute("msg", String.format("Você foi promovido para %s.", newRank.getDescription()));
        } catch(RegraNegocioException e) {
            model.addAttribute("cl", e.getCl());
            model.addAttribute("msg", e.getMessage());
        }
        return "mensagem";
    }

    @RequestMapping(value = "/user/rankdown", method = RequestMethod.GET)
    public String rankDown(Model model, @AuthenticationPrincipal User user) {
        Usuario atual = servico.findByEmail(user.getUsername());try {
            Rank newRank = rankServico.rankDown(atual);
            model.addAttribute("cl", "is-warning");
            model.addAttribute("msg", String.format("Você foi despromovido para %s.", newRank.getDescription()));
        } catch(RegraNegocioException e) {
            model.addAttribute("cl", e.getCl());
            model.addAttribute("msg", e.getMessage());
        }
        return "mensagem";
    }
}
