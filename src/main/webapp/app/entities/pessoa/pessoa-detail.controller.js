(function() {
    'use strict';

    angular
        .module('tccmanagerApp')
        .controller('PessoaDetailController', PessoaDetailController);

    PessoaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pessoa', 'Usuario'];

    function PessoaDetailController($scope, $rootScope, $stateParams, previousState, entity, Pessoa, Usuario) {
        var vm = this;

        vm.pessoa = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tccmanagerApp:pessoaUpdate', function(event, result) {
            vm.pessoa = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
