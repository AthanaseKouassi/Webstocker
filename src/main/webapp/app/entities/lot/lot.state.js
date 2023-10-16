(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lot', {
            parent: 'entity',
            url: '/lot',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.lot.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lot/lots.html',
                    controller: 'LotController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lot');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lot-detail', {
            parent: 'entity',
            url: '/lot/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.lot.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lot/lot-detail.html',
                    controller: 'LotDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lot');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lot', function($stateParams, Lot) {
                    return Lot.get({id : $stateParams.id});
                }]
            }
        })
        .state('lot.new', {
            parent: 'lot',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lot/lot-dialog.html',
                    controller: 'LotDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numeroLot: null,
                                dateFabrication: null,
                                datePeremption: null,
                                descriptionLot: null,
                                quantiteLot: null,
                                quantiteCartonLot: null,
                                quantiteSortie: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lot', null, { reload: true });
                }, function() {
                    $state.go('lot');
                });
            }]
        })
        .state('lot.edit', {
            parent: 'lot',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lot/lot-dialog.html',
                    controller: 'LotDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lot', function(Lot) {
                            return Lot.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lot', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lot.delete', {
            parent: 'lot',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lot/lot-delete-dialog.html',
                    controller: 'LotDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lot', function(Lot) {
                            return Lot.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lot', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
