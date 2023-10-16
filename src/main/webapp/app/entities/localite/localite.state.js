(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('localite', {
            parent: 'entity',
            url: '/localite',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.localite.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/localite/localites.html',
                    controller: 'LocaliteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('localite');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('localite-detail', {
            parent: 'entity',
            url: '/localite/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.localite.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/localite/localite-detail.html',
                    controller: 'LocaliteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('localite');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Localite', function($stateParams, Localite) {
                    return Localite.get({id : $stateParams.id});
                }]
            }
        })
        .state('localite.new', {
            parent: 'localite',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/localite/localite-dialog.html',
                    controller: 'LocaliteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('localite', null, { reload: true });
                }, function() {
                    $state.go('localite');
                });
            }]
        })
        .state('localite.edit', {
            parent: 'localite',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/localite/localite-dialog.html',
                    controller: 'LocaliteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Localite', function(Localite) {
                            return Localite.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('localite', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('localite.delete', {
            parent: 'localite',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/localite/localite-delete-dialog.html',
                    controller: 'LocaliteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Localite', function(Localite) {
                            return Localite.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('localite', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
