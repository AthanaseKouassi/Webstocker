(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ville', {
            parent: 'entity',
            url: '/ville?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ville.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ville/villes.html',
                    controller: 'VilleController',
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
                    $translatePartialLoader.addPart('ville');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ville-detail', {
            parent: 'entity',
            url: '/ville/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ville.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ville/ville-detail.html',
                    controller: 'VilleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ville');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Ville', function($stateParams, Ville) {
                    return Ville.get({id : $stateParams.id});
                }]
            }
        })
        .state('ville.new', {
            parent: 'ville',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ville/ville-dialog.html',
                    controller: 'VilleDialogController',
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
                    $state.go('ville', null, { reload: true });
                }, function() {
                    $state.go('ville');
                });
            }]
        })
        .state('ville.edit', {
            parent: 'ville',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ville/ville-dialog.html',
                    controller: 'VilleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ville', function(Ville) {
                            return Ville.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ville', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ville.delete', {
            parent: 'ville',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ville/ville-delete-dialog.html',
                    controller: 'VilleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ville', function(Ville) {
                            return Ville.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ville', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
