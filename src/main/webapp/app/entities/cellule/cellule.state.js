(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cellule', {
            parent: 'entity',
            url: '/cellule?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.cellule.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cellule/cellules.html',
                    controller: 'CelluleController',
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
                    $translatePartialLoader.addPart('cellule');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cellule-detail', {
            parent: 'entity',
            url: '/cellule/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.cellule.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cellule/cellule-detail.html',
                    controller: 'CelluleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cellule');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cellule', function($stateParams, Cellule) {
                    return Cellule.get({id : $stateParams.id});
                }]
            }
        })
        .state('cellule.new', {
            parent: 'cellule',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cellule/cellule-dialog.html',
                    controller: 'CelluleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cellule', null, { reload: true });
                }, function() {
                    $state.go('cellule');
                });
            }]
        })
        .state('cellule.edit', {
            parent: 'cellule',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cellule/cellule-dialog.html',
                    controller: 'CelluleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cellule', function(Cellule) {
                            return Cellule.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('cellule', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cellule.delete', {
            parent: 'cellule',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cellule/cellule-delete-dialog.html',
                    controller: 'CelluleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cellule', function(Cellule) {
                            return Cellule.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('cellule', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
