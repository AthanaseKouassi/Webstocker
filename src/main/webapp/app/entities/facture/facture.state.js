(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('facture', {
            parent: 'entity',
            url: '/facture',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.facture.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/facture/factures.html',
                    controller: 'FactureController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('facture');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('facture-detail', {
            parent: 'entity',
            url: '/facture/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.facture.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/facture/facture-detail.html',
                    controller: 'FactureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('facture');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Facture', function($stateParams, Facture) {
                    return Facture.get({id : $stateParams.id});
                }]
            }
        })
        .state('facture.new', {
            parent: 'facture',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/facture/facture-dialog.html',
                    controller: 'FactureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateFacture: null,
                                valeurRemise: null,
                                delaiPaiement: null,
                                dateLimitePaiement: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('facture', null, { reload: true });
                }, function() {
                    $state.go('facture');
                });
            }]
        })
        .state('facture.edit', {
            parent: 'facture',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/facture/facture-dialog.html',
                    controller: 'FactureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Facture', function(Facture) {
                            return Facture.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('facture', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('facture.delete', {
            parent: 'facture',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/facture/facture-delete-dialog.html',
                    controller: 'FactureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Facture', function(Facture) {
                            return Facture.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('facture', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
