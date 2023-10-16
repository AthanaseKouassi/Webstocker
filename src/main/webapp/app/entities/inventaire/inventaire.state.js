(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('inventaire', {
            parent: 'entity',
            url: '/inventaire?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.inventaire.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inventaire/inventaires.html',
                    controller: 'InventaireController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inventaire');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('inventaire-detail', {
            parent: 'inventaire',
            url: '/inventaire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.inventaire.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inventaire/inventaire-detail.html',
                    controller: 'InventaireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inventaire');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Inventaire', function($stateParams, Inventaire) {
                    return Inventaire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'inventaire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('inventaire-detail.edit', {
            parent: 'inventaire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inventaire/inventaire-dialog.html',
                    controller: 'InventaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Inventaire', function(Inventaire) {
                            return Inventaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inventaire.new', {
            parent: 'inventaire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inventaire/inventaire-dialog.html',
                    controller: 'InventaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateInventaire: null,
                                stockFinalTheorique: null,
                                stockReel: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('inventaire', null, { reload: 'inventaire' });
                }, function() {
                    $state.go('inventaire');
                });
            }]
        })
        .state('inventaire.edit', {
            parent: 'inventaire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inventaire/inventaire-dialog.html',
                    controller: 'InventaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Inventaire', function(Inventaire) {
                            return Inventaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inventaire', null, { reload: 'inventaire' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inventaire.delete', {
            parent: 'inventaire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inventaire/inventaire-delete-dialog.html',
                    controller: 'InventaireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Inventaire', function(Inventaire) {
                            return Inventaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inventaire', null, { reload: 'inventaire' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
