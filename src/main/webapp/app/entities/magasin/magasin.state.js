(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('magasin', {
            parent: 'entity',
            url: '/magasin',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.magasin.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/magasin/magasins.html',
                    controller: 'MagasinController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('magasin');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('magasin-detail', {
            parent: 'entity',
            url: '/magasin/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.magasin.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/magasin/magasin-detail.html',
                    controller: 'MagasinDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('magasin');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Magasin', function($stateParams, Magasin) {
                    return Magasin.get({id : $stateParams.id});
                }]
            }
        })
        .state('magasin.new', {
            parent: 'magasin',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/magasin/magasin-dialog.html',
                    controller: 'MagasinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nomMagasin: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('magasin', null, { reload: true });
                }, function() {
                    $state.go('magasin');
                });
            }]
        })
        .state('magasin.edit', {
            parent: 'magasin',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/magasin/magasin-dialog.html',
                    controller: 'MagasinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Magasin', function(Magasin) {
                            return Magasin.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('magasin', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('magasin.delete', {
            parent: 'magasin',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/magasin/magasin-delete-dialog.html',
                    controller: 'MagasinDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Magasin', function(Magasin) {
                            return Magasin.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('magasin', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
