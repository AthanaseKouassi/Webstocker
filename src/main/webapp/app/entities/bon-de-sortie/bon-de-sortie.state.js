(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bon-de-sortie', {
            parent: 'entity',
            url: '/bon-de-sortie?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.bonDeSortie.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bon-de-sortie/bon-de-sorties.html',
                    controller: 'BonDeSortieController',
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
                    $translatePartialLoader.addPart('bonDeSortie');
                    $translatePartialLoader.addPart('typeSortie');
                    $translatePartialLoader.addPart('typeVente');
                    $translatePartialLoader.addPart('statusTransfert');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bon-de-sortie-detail', {
            parent: 'bon-de-sortie',
            url: '/bon-de-sortie/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.bonDeSortie.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bon-de-sortie/bon-de-sortie-detail.html',
                    controller: 'BonDeSortieDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bonDeSortie');
                    $translatePartialLoader.addPart('typeSortie');
                    $translatePartialLoader.addPart('typeVente');
                    $translatePartialLoader.addPart('statusTransfert');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BonDeSortie', function($stateParams, BonDeSortie) {
                    return BonDeSortie.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bon-de-sortie',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bon-de-sortie-detail.edit', {
            parent: 'bon-de-sortie-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bon-de-sortie/bon-de-sortie-dialog.html',
                    controller: 'BonDeSortieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BonDeSortie', function(BonDeSortie) {
                            return BonDeSortie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bon-de-sortie.new', {
            parent: 'bon-de-sortie',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bon-de-sortie/bon-de-sortie-dialog.html',
                    controller: 'BonDeSortieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numero: null,
                                daateCreation: null,
                                typeSortie: null,
                                typeVente: null,
                                printStatus: null,
                                numeroFactureNormalise: null,

                                statusTranfert: null,
                                dateReception: null,

                                dateReceptionTransfert: null,

                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bon-de-sortie', null, { reload: 'bon-de-sortie' });
                }, function() {
                    $state.go('bon-de-sortie');
                });
            }]
        })
        .state('bon-de-sortie.edit', {
            parent: 'bon-de-sortie',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bon-de-sortie/bon-de-sortie-dialog.html',
                    controller: 'BonDeSortieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BonDeSortie', function(BonDeSortie) {
                            return BonDeSortie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bon-de-sortie', null, { reload: 'bon-de-sortie' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bon-de-sortie.delete', {
            parent: 'bon-de-sortie',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bon-de-sortie/bon-de-sortie-delete-dialog.html',
                    controller: 'BonDeSortieDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BonDeSortie', function(BonDeSortie) {
                            return BonDeSortie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bon-de-sortie', null, { reload: 'bon-de-sortie' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
