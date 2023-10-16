(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('commande', {
            parent: 'entity',
            url: '/commande',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.commande.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commande/commandes.html',
                    controller: 'CommandeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commande');
                    $translatePartialLoader.addPart('statutCommande');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('commande-detail', {
            parent: 'entity',
            url: '/commande/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.commande.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commande/commande-detail.html',
                    controller: 'CommandeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commande');
                    $translatePartialLoader.addPart('statutCommande');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Commande', function($stateParams, Commande) {
                    return Commande.get({id : $stateParams.id});
                }]
            }
        })
        .state('commande.new', {
            parent: 'commande',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande/commande-dialog.html',
                    controller: 'CommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateCommande: null,
                                numeroCommande: null,
                                quantiteCommande: null,
                                valeurCommande: null,
                                statut: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('commande', null, { reload: true });
                }, function() {
                    $state.go('commande');
                });
            }]
        })
        .state('commande.edit', {
            parent: 'commande',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande/commande-dialog.html',
                    controller: 'CommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Commande', function(Commande) {
                            return Commande.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('commande', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commande.delete', {
            parent: 'commande',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande/commande-delete-dialog.html',
                    controller: 'CommandeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Commande', function(Commande) {
                            return Commande.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('commande', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
