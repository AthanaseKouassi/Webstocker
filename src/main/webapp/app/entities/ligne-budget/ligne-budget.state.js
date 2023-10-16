(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ligne-budget', {
            parent: 'entity',
            url: '/ligne-budget?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ligneBudget.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-budget/ligne-budgets.html',
                    controller: 'LigneBudgetController',
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
                    $translatePartialLoader.addPart('ligneBudget');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ligne-budget-detail', {
            parent: 'entity',
            url: '/ligne-budget/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ligneBudget.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-budget/ligne-budget-detail.html',
                    controller: 'LigneBudgetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ligneBudget');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LigneBudget', function($stateParams, LigneBudget) {
                    return LigneBudget.get({id : $stateParams.id});
                }]
            }
        })
        .state('ligne-budget.new', {
            parent: 'ligne-budget',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-budget/ligne-budget-dialog.html',
                    controller: 'LigneBudgetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelleLigneBudget: null,
                                montantLigneBudget: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ligne-budget', null, { reload: true });
                }, function() {
                    $state.go('ligne-budget');
                });
            }]
        })
        .state('ligne-budget.edit', {
            parent: 'ligne-budget',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-budget/ligne-budget-dialog.html',
                    controller: 'LigneBudgetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LigneBudget', function(LigneBudget) {
                            return LigneBudget.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-budget', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ligne-budget.delete', {
            parent: 'ligne-budget',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-budget/ligne-budget-delete-dialog.html',
                    controller: 'LigneBudgetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LigneBudget', function(LigneBudget) {
                            return LigneBudget.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-budget', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
