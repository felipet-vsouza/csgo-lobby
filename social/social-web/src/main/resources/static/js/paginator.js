class Paginator {
    constructor(father, token) {
        this.token = token;
        this.father = father;
        this.definePaginator();
    }

    definePaginator() {
        let self = this;
        let size = $('#totalsize').text();
        let atual = $('#curpage').text();
        atual = parseInt(atual);

        $('.pagbutton').click(function () {
            let page = $(this).text();
            let size = $('#totalsize').text();
            self.father.wanderThroughPages(page, size, self.token);
        });
        $('#butnext').click(function () {
            let page = atual + 1;
            self.father.wanderThroughPages(page, size, self.token);
        });
        $('#butprev').click(function () {
            let page = atual - 1;
            self.father.wanderThroughPages(page, size, self.token);
        });
    }
}