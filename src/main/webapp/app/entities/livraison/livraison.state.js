(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('livraison', {
            parent: 'entity',
            url: '/livraison',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.livraison.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/livraison/livraisons.html',
                    controller: 'LivraisonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('livraison');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('livraison-detail', {
            parent: 'entity',
            url: '/livraison/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.livraison.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/livraison/livraison-detail.html',
                    controller: 'LivraisonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('livraison');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Livraison', function($stateParams, Livraison) {
                    return Livraison.get({id : $stateParams.id});
                }]
            }
        })
        .state('livraison.new', {
            parent: 'livraison',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/livraison/livraison-dialog.html',
                    controller: 'LivraisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateLivraison: null,
                                descriptionLivraison: null,
                                numeroLivraison: null,
                                valeurLivraison: null,
                                fraisTest: null,
                                fraisTransit: null,
                                fraisAssuranceLocale: null,
                                fraisManutention: null,
                                valeurLivraisonDevise: null,
                                devise: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('livraison', null, { reload: true });
                }, function() {
                    $state.go('livraison');
                });
            }]
        })
        .state('livraison.edit', {
            parent: 'livraison',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/livraison/livraison-dialog.html',
                    controller: 'LivraisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Livraison', function(Livraison) {
                            return Livraison.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('livraison', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('livraison.delete', {
            parent: 'livraison',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/livraison/livraison-delete-dialog.html',
                    controller: 'LivraisonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Livraison', function(Livraison) {
                            return Livraison.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('livraison', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
