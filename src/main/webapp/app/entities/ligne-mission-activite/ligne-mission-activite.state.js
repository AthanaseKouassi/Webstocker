(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ligne-mission-activite', {
            parent: 'entity',
            url: '/ligne-mission-activite?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ligneMissionActivite.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-mission-activite/ligne-mission-activites.html',
                    controller: 'LigneMissionActiviteController',
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
                    $translatePartialLoader.addPart('ligneMissionActivite');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ligne-mission-activite-detail', {
            parent: 'entity',
            url: '/ligne-mission-activite/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ligneMissionActivite.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-mission-activite/ligne-mission-activite-detail.html',
                    controller: 'LigneMissionActiviteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ligneMissionActivite');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LigneMissionActivite', function($stateParams, LigneMissionActivite) {
                    return LigneMissionActivite.get({id : $stateParams.id});
                }]
            }
        })
        .state('ligne-mission-activite.new', {
            parent: 'ligne-mission-activite',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-mission-activite/ligne-mission-activite-dialog.html',
                    controller: 'LigneMissionActiviteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                resultatObtenu: null,
                                dateResultat: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ligne-mission-activite', null, { reload: true });
                }, function() {
                    $state.go('ligne-mission-activite');
                });
            }]
        })
        .state('ligne-mission-activite.edit', {
            parent: 'ligne-mission-activite',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-mission-activite/ligne-mission-activite-dialog.html',
                    controller: 'LigneMissionActiviteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LigneMissionActivite', function(LigneMissionActivite) {
                            return LigneMissionActivite.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-mission-activite', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ligne-mission-activite.delete', {
            parent: 'ligne-mission-activite',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-mission-activite/ligne-mission-activite-delete-dialog.html',
                    controller: 'LigneMissionActiviteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LigneMissionActivite', function(LigneMissionActivite) {
                            return LigneMissionActivite.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-mission-activite', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
