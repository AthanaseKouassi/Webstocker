(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ligne-bon-de-sortie', {
            parent: 'entity',
            url: '/ligne-bon-de-sortie?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ligneBonDeSortie.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-bon-de-sortie/ligne-bon-de-sorties.html',
                    controller: 'LigneBonDeSortieController',
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
                    $translatePartialLoader.addPart('ligneBonDeSortie');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ligne-bon-de-sortie-detail', {
            parent: 'entity',
            url: '/ligne-bon-de-sortie/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ligneBonDeSortie.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-bon-de-sortie/ligne-bon-de-sortie-detail.html',
                    controller: 'LigneBonDeSortieDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ligneBonDeSortie');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LigneBonDeSortie', function($stateParams, LigneBonDeSortie) {
                    return LigneBonDeSortie.get({id : $stateParams.id});
                }]
            }
        })
        .state('ligne-bon-de-sortie.new', {
            parent: 'ligne-bon-de-sortie',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-bon-de-sortie/ligne-bon-de-sortie-dialog.html',
                    controller: 'LigneBonDeSortieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                quantite: null,
                                prixVente: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ligne-bon-de-sortie', null, { reload: true });
                }, function() {
                    $state.go('ligne-bon-de-sortie');
                });
            }]
        })
        .state('ligne-bon-de-sortie.edit', {
            parent: 'ligne-bon-de-sortie',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-bon-de-sortie/ligne-bon-de-sortie-dialog.html',
                    controller: 'LigneBonDeSortieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LigneBonDeSortie', function(LigneBonDeSortie) {
                            return LigneBonDeSortie.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-bon-de-sortie', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ligne-bon-de-sortie.delete', {
            parent: 'ligne-bon-de-sortie',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-bon-de-sortie/ligne-bon-de-sortie-delete-dialog.html',
                    controller: 'LigneBonDeSortieDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LigneBonDeSortie', function(LigneBonDeSortie) {
                            return LigneBonDeSortie.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-bon-de-sortie', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
