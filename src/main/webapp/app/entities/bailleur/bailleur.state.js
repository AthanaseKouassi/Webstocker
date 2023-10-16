(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bailleur', {
            parent: 'entity',
            url: '/bailleur',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.bailleur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bailleur/bailleurs.html',
                    controller: 'BailleurController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bailleur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bailleur-detail', {
            parent: 'entity',
            url: '/bailleur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.bailleur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bailleur/bailleur-detail.html',
                    controller: 'BailleurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bailleur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bailleur', function($stateParams, Bailleur) {
                    return Bailleur.get({id : $stateParams.id});
                }]
            }
        })
        .state('bailleur.new', {
            parent: 'bailleur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bailleur/bailleur-dialog.html',
                    controller: 'BailleurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nomBailleur: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bailleur', null, { reload: true });
                }, function() {
                    $state.go('bailleur');
                });
            }]
        })
        .state('bailleur.edit', {
            parent: 'bailleur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bailleur/bailleur-dialog.html',
                    controller: 'BailleurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bailleur', function(Bailleur) {
                            return Bailleur.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('bailleur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bailleur.delete', {
            parent: 'bailleur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bailleur/bailleur-delete-dialog.html',
                    controller: 'BailleurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bailleur', function(Bailleur) {
                            return Bailleur.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('bailleur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
