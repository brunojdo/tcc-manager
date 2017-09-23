(function() {
    'use strict';

    angular
        .module('tccmanagerApp')
        .controller('UsuarioDialogController', UsuarioDialogController);

    UsuarioDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Usuario', 'Pessoa'];

    function UsuarioDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Usuario, Pessoa) {
        var vm = this;

        vm.usuario = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pessoas = Pessoa.query({filter: 'usuario-is-null'});
        $q.all([vm.usuario.$promise, vm.pessoas.$promise]).then(function() {
            if (!vm.usuario.pessoa || !vm.usuario.pessoa.id) {
                return $q.reject();
            }
            return Pessoa.get({id : vm.usuario.pessoa.id}).$promise;
        }).then(function(pessoa) {
            vm.pessoas.push(pessoa);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.usuario.id !== null) {
                Usuario.update(vm.usuario, onSaveSuccess, onSaveError);
            } else {
                Usuario.save(vm.usuario, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tccmanagerApp:usuarioUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
