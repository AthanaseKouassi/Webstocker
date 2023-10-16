(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reglement', {
            parent: 'entity',
            url: '/reglement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.reglement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reglement/reglements.html',
                    controller: 'ReglementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reglement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reglement-detail', {
            parent: 'entity',
            url: '/reglement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.reglement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reglement/reglement-detail.html',
                    controller: 'ReglementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reglement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Reglement', function($stateParams, Reglement) {
                    return Reglement.get({id : $stateParams.id});
                }]
            }
        })
        .state('reglement.new', {
            parent: 'reglement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reglement/reglement-dialog.html',
                    controller: 'ReglementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateReglement: null,
                                montantReglement: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reglement', null, { reload: true });
                }, function() {
                    $state.go('reglement');
                });
            }]
        })
        .state('reglement.edit', {
            parent: 'reglement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reglement/reglement-dialog.html',
                    controller: 'ReglementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Reglement', function(Reglement) {
                            return Reglement.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('reglement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reglement.delete', {
            parent: 'reglement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reglement/reglement-delete-dialog.html',
                    controller: 'ReglementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Reglement', function(Reglement) {
                            return Reglement.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('reglement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
