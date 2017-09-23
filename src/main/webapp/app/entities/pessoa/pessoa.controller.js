(function() {
    'use strict';

    angular
        .module('tccmanagerApp')
        .controller('PessoaController', PessoaController);

    PessoaController.$inject = ['Pessoa'];

    function PessoaController(Pessoa) {

        var vm = this;

        vm.pessoas = [];

        loadAll();

        function loadAll() {
            Pessoa.query(function(result) {
                vm.pessoas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
