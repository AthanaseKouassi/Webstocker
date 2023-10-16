(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('region', {
            parent: 'entity',
            url: '/region?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.region.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/region/regions.html',
                    controller: 'RegionController',
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
                    $translatePartialLoader.addPart('region');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('region-detail', {
            parent: 'entity',
            url: '/region/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.region.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/region/region-detail.html',
                    controller: 'RegionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('region');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Region', function($stateParams, Region) {
                    return Region.get({id : $stateParams.id});
                }]
            }
        })
        .state('region.new', {
            parent: 'region',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/region/region-dialog.html',
                    controller: 'RegionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('region', null, { reload: true });
                }, function() {
                    $state.go('region');
                });
            }]
        })
        .state('region.edit', {
            parent: 'region',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/region/region-dialog.html',
                    controller: 'RegionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Region', function(Region) {
                            return Region.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('region', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('region.delete', {
            parent: 'region',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/region/region-delete-dialog.html',
                    controller: 'RegionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Region', function(Region) {
                            return Region.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('region', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
