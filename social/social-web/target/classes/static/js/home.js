class Home {
    constructor() {
        this.defineNewPostBind();
        this.defineSubmitPostBinds();
        this.defineRefreshBinds();
    }

    defineNewPostBind() {
        this.newpostbutton = $('#newpost-button');
        this.newpostform = $('#newpost-form');
        let self = this;
        this.newpostbutton.click(function () {
            self.newpostbutton.toggleClass('is-disabled');
            self.newpostform.toggle();
        });
    }

    defineSubmitPostBinds() {
        this.submitpostbutton = $('#newpost-submit');
        this.closepostbutton = $('#newpost-close');
        this.posttitle = $('#newpost-titulo');
        this.postbody = $('#newpost-corpo');
        this.caption = $('#caption');
        let self = this;

        let clearFields = function () {
            self.posttitle.val('');
            self.postbody.val('');
        }

        this.submitpostbutton.click(function () {
            let title = self.posttitle.val();
            let body = self.postbody.val();
            $.post('/post/new', { titulo: title, corpo: body })
                .then(function () {
                    clearFields();
                    self.caption.text('Post inserido com sucesso.');
                    self.submitpostbutton
                        .removeClass('is-danger')
                        .addClass('is-success')
                        .text('Postar');
                    self.refreshPosts();
                })
                .fail(function (err) {
                    if (!err.responseText) {
                        self.caption.text('Oh oh - Há algum problema com a sua publicação.');
                    } else {
                        self.caption.text(`Oh oh - ${err.responseText}`);
                    }
                    self.submitpostbutton
                        .removeClass('is-success')
                        .addClass('is-danger')
                        .text('Tentar novamente');
                });
        });
        this.closepostbutton.click(function () {
            self.newpostbutton.toggleClass('is-disabled');
            self.newpostform.toggle();
            self.caption.text('');
            self.submitpostbutton
                .removeClass('is-danger')
                .addClass('is-success')
                .text('Postar');
            clearFields();
        });
    }

    defineRefreshBinds() {
        this.refreshButton = $('#refresh-posts');
        let self = this;
        this.refreshButton.click(function () {
            self.refreshPosts();
        });
        setInterval(self.refreshPosts.bind(self), 60000);
    }

    refreshPosts() {
        this.refreshButton
            .toggleClass('is-disabled')
            .toggleClass('is-loading');
        let self = this;
        $.get('post/refresh')
            .then(res => {
                self.refreshButton
                    .toggleClass('is-disabled')
                    .toggleClass('is-loading');
                $('#postlist').html(res);
                return $.get('post/refresh/total');
            })
            .then(res => {
                $('#totalposts').text(res);
            })
            .fail(err => {
                console.error('Oh oh, houve um erro: ', err);
                self.refreshButton
                    .toggleClass('is-disabled')
                    .toggleClass('is-loading');
            });
    }
}

$(function () {
    new Home();
});