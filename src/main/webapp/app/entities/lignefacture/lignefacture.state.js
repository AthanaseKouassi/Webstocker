(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lignefacture', {
            parent: 'entity',
            url: '/lignefacture',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.lignefacture.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lignefacture/lignefactures.html',
                    controller: 'LignefactureController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lignefacture');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lignefacture-detail', {
            parent: 'entity',
            url: '/lignefacture/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.lignefacture.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lignefacture/lignefacture-detail.html',
                    controller: 'LignefactureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lignefacture');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lignefacture', function($stateParams, Lignefacture) {
                    return Lignefacture.get({id : $stateParams.id});
                }]
            }
        })
        .state('lignefacture.new', {
            parent: 'lignefacture',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lignefacture/lignefacture-dialog.html',
                    controller: 'LignefactureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                quantiteFacture: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lignefacture', null, { reload: true });
                }, function() {
                    $state.go('lignefacture');
                });
            }]
        })
        .state('lignefacture.edit', {
            parent: 'lignefacture',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lignefacture/lignefacture-dialog.html',
                    controller: 'LignefactureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lignefacture', function(Lignefacture) {
                            return Lignefacture.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lignefacture', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lignefacture.delete', {
            parent: 'lignefacture',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lignefacture/lignefacture-delete-dialog.html',
                    controller: 'LignefactureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lignefacture', function(Lignefacture) {
                            return Lignefacture.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lignefacture', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
