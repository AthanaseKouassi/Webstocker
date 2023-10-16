(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('commune', {
            parent: 'entity',
            url: '/commune?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.commune.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commune/communes.html',
                    controller: 'CommuneController',
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
                    $translatePartialLoader.addPart('commune');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('commune-detail', {
            parent: 'entity',
            url: '/commune/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.commune.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commune/commune-detail.html',
                    controller: 'CommuneDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commune');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Commune', function($stateParams, Commune) {
                    return Commune.get({id : $stateParams.id});
                }]
            }
        })
        .state('commune.new', {
            parent: 'commune',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commune/commune-dialog.html',
                    controller: 'CommuneDialogController',
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
                    $state.go('commune', null, { reload: true });
                }, function() {
                    $state.go('commune');
                });
            }]
        })
        .state('commune.edit', {
            parent: 'commune',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commune/commune-dialog.html',
                    controller: 'CommuneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Commune', function(Commune) {
                            return Commune.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('commune', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commune.delete', {
            parent: 'commune',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commune/commune-delete-dialog.html',
                    controller: 'CommuneDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Commune', function(Commune) {
                            return Commune.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('commune', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
