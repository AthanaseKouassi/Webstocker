(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('objectifs', {
            parent: 'entity',
            url: '/objectifs',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.objectifs.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/objectifs/objectifs.html',
                    controller: 'ObjectifsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('objectifs');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('objectifs-detail', {
            parent: 'entity',
            url: '/objectifs/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.objectifs.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/objectifs/objectifs-detail.html',
                    controller: 'ObjectifsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('objectifs');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Objectifs', function($stateParams, Objectifs) {
                    return Objectifs.get({id : $stateParams.id});
                }]
            }
        })
        .state('objectifs.new', {
            parent: 'objectifs',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/objectifs/objectifs-dialog.html',
                    controller: 'ObjectifsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                periode: null,
                                quantiteAttendue: null,
                                quantiteObtenu: null,
                                taux: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('objectifs', null, { reload: true });
                }, function() {
                    $state.go('objectifs');
                });
            }]
        })
        .state('objectifs.edit', {
            parent: 'objectifs',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/objectifs/objectifs-dialog.html',
                    controller: 'ObjectifsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Objectifs', function(Objectifs) {
                            return Objectifs.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('objectifs', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('objectifs.delete', {
            parent: 'objectifs',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/objectifs/objectifs-delete-dialog.html',
                    controller: 'ObjectifsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Objectifs', function(Objectifs) {
                            return Objectifs.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('objectifs', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
