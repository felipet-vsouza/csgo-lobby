class Find {
    constructor() {
        this.buttonFind = $("#submit-find");
        this.textFind = $("#text-find");
        this.defineDefaultBinds();
    }

    defineDefaultBinds() {
        let self = this;
        this.buttonFind.click(self.findByText.bind(self));
        this.textFind.keydown(e => {
            if (e.keyCode == 13) {
                self.findByText.bind(self)();
            }
        });
    }

    findByText() {
        let token = this.textFind.val();
        let self = this;
        $.get('/find/user', { token: token })
            .then(ret => {
                $('#box-usuarios').html(ret);
                new Paginator(self, token);
            })
            .fail(err => {
                console.error("Oh-oh, an error ocurred: ", err);
            });
    }

    wanderThroughPages(page, size, token) {
        let self = this;
        $.get('/find/user', { token: token, page: page, size: size })
            .then(ret => {
                $('#box-usuarios').html(ret);
                new Paginator(self, token);
            })
            .fail(err => {
                console.error("Oh-oh, an error ocurred: ", err);
            });
    }
}

$(function () {
    main.defineReponsiveNavToggle();
    main.defineDefaultLoader();
    new Find();
});