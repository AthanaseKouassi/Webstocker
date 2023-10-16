(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ligneprixproduit', {
            parent: 'entity',
            url: '/ligneprixproduit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ligneprixproduit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligneprixproduit/ligneprixproduits.html',
                    controller: 'LigneprixproduitController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ligneprixproduit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ligneprixproduit-detail', {
            parent: 'entity',
            url: '/ligneprixproduit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ligneprixproduit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligneprixproduit/ligneprixproduit-detail.html',
                    controller: 'LigneprixproduitDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ligneprixproduit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Ligneprixproduit', function($stateParams, Ligneprixproduit) {
                    return Ligneprixproduit.get({id : $stateParams.id});
                }]
            }
        })
        .state('ligneprixproduit.new', {
            parent: 'ligneprixproduit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligneprixproduit/ligneprixproduit-dialog.html',
                    controller: 'LigneprixproduitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ligneprixproduit', null, { reload: true });
                }, function() {
                    $state.go('ligneprixproduit');
                });
            }]
        })
        .state('ligneprixproduit.edit', {
            parent: 'ligneprixproduit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligneprixproduit/ligneprixproduit-dialog.html',
                    controller: 'LigneprixproduitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ligneprixproduit', function(Ligneprixproduit) {
                            return Ligneprixproduit.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligneprixproduit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ligneprixproduit.delete', {
            parent: 'ligneprixproduit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligneprixproduit/ligneprixproduit-delete-dialog.html',
                    controller: 'LigneprixproduitDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ligneprixproduit', function(Ligneprixproduit) {
                            return Ligneprixproduit.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligneprixproduit', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
