(function() {
    'use strict';

    angular
        .module('tccmanagerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pessoa', {
            parent: 'entity',
            url: '/pessoa',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Pessoas'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pessoa/pessoas.html',
                    controller: 'PessoaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('pessoa-detail', {
            parent: 'pessoa',
            url: '/pessoa/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Pessoa'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pessoa/pessoa-detail.html',
                    controller: 'PessoaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Pessoa', function($stateParams, Pessoa) {
                    return Pessoa.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pessoa',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pessoa-detail.edit', {
            parent: 'pessoa-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pessoa/pessoa-dialog.html',
                    controller: 'PessoaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pessoa', function(Pessoa) {
                            return Pessoa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pessoa.new', {
            parent: 'pessoa',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pessoa/pessoa-dialog.html',
                    controller: 'PessoaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                primeiroNome: null,
                                segundoNome: null,
                                matricula: null,
                                email: null,
                                numeroContato: null,
                                diponibilidade: null,
                                tipoDeUsuario: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pessoa', null, { reload: 'pessoa' });
                }, function() {
                    $state.go('pessoa');
                });
            }]
        })
        .state('pessoa.edit', {
            parent: 'pessoa',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pessoa/pessoa-dialog.html',
                    controller: 'PessoaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pessoa', function(Pessoa) {
                            return Pessoa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pessoa', null, { reload: 'pessoa' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pessoa.delete', {
            parent: 'pessoa',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pessoa/pessoa-delete-dialog.html',
                    controller: 'PessoaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pessoa', function(Pessoa) {
                            return Pessoa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pessoa', null, { reload: 'pessoa' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
