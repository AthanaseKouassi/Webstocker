(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lignelivraison', {
            parent: 'entity',
            url: '/lignelivraison',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.lignelivraison.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lignelivraison/lignelivraisons.html',
                    controller: 'LignelivraisonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lignelivraison');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lignelivraison-detail', {
            parent: 'entity',
            url: '/lignelivraison/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.lignelivraison.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lignelivraison/lignelivraison-detail.html',
                    controller: 'LignelivraisonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lignelivraison');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lignelivraison', function($stateParams, Lignelivraison) {
                    return Lignelivraison.get({id : $stateParams.id});
                }]
            }
        })
        .state('lignelivraison.new', {
            parent: 'lignelivraison',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lignelivraison/lignelivraison-dialog.html',
                    controller: 'LignelivraisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                quantiteLotLivre: null,
                                quantiteCartonLot: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lignelivraison', null, { reload: true });
                }, function() {
                    $state.go('lignelivraison');
                });
            }]
        })
        .state('lignelivraison.edit', {
            parent: 'lignelivraison',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lignelivraison/lignelivraison-dialog.html',
                    controller: 'LignelivraisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lignelivraison', function(Lignelivraison) {
                            return Lignelivraison.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lignelivraison', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lignelivraison.delete', {
            parent: 'lignelivraison',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lignelivraison/lignelivraison-delete-dialog.html',
                    controller: 'LignelivraisonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lignelivraison', function(Lignelivraison) {
                            return Lignelivraison.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lignelivraison', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
