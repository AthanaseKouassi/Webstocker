(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ligneobjectifvente', {
            parent: 'entity',
            url: '/ligneobjectifvente',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ligneobjectifvente.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligneobjectifvente/ligneobjectifventes.html',
                    controller: 'LigneobjectifventeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ligneobjectifvente');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ligneobjectifvente-detail', {
            parent: 'entity',
            url: '/ligneobjectifvente/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.ligneobjectifvente.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligneobjectifvente/ligneobjectifvente-detail.html',
                    controller: 'LigneobjectifventeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ligneobjectifvente');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Ligneobjectifvente', function($stateParams, Ligneobjectifvente) {
                    return Ligneobjectifvente.get({id : $stateParams.id});
                }]
            }
        })
        .state('ligneobjectifvente.new', {
            parent: 'ligneobjectifvente',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligneobjectifvente/ligneobjectifvente-dialog.html',
                    controller: 'LigneobjectifventeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                quantiteObjectif: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ligneobjectifvente', null, { reload: true });
                }, function() {
                    $state.go('ligneobjectifvente');
                });
            }]
        })
        .state('ligneobjectifvente.edit', {
            parent: 'ligneobjectifvente',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligneobjectifvente/ligneobjectifvente-dialog.html',
                    controller: 'LigneobjectifventeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ligneobjectifvente', function(Ligneobjectifvente) {
                            return Ligneobjectifvente.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligneobjectifvente', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ligneobjectifvente.delete', {
            parent: 'ligneobjectifvente',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligneobjectifvente/ligneobjectifvente-delete-dialog.html',
                    controller: 'LigneobjectifventeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ligneobjectifvente', function(Ligneobjectifvente) {
                            return Ligneobjectifvente.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligneobjectifvente', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
