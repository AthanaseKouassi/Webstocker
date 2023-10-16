(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mission', {
            parent: 'entity',
            url: '/mission?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.mission.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mission/missions.html',
                    controller: 'MissionController',
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
                    $translatePartialLoader.addPart('mission');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('mission-detail', {
            parent: 'entity',
            url: '/mission/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.mission.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mission/mission-detail.html',
                    controller: 'MissionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mission');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Mission', function($stateParams, Mission) {
                    return Mission.get({id : $stateParams.id});
                }]
            }
        })
        .state('mission.new', {
            parent: 'mission',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mission/mission-dialog.html',
                    controller: 'MissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                dateDebut: null,
                                dateFin: null,
                                objectifGeneral: null,
                                objectifSpecifique: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mission', null, { reload: true });
                }, function() {
                    $state.go('mission');
                });
            }]
        })
        .state('mission.edit', {
            parent: 'mission',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mission/mission-dialog.html',
                    controller: 'MissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Mission', function(Mission) {
                            return Mission.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('mission', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mission.delete', {
            parent: 'mission',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mission/mission-delete-dialog.html',
                    controller: 'MissionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Mission', function(Mission) {
                            return Mission.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('mission', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
