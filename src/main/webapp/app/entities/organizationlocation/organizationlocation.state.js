(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('organizationlocation', {
            parent: 'entity',
            url: '/organizationlocation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fadsiiApp.organizationlocation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/organizationlocation/organizationlocations.html',
                    controller: 'OrganizationlocationController',
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
                    $translatePartialLoader.addPart('organizationlocation');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('organizationlocation-detail', {
            parent: 'entity',
            url: '/organizationlocation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fadsiiApp.organizationlocation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/organizationlocation/organizationlocation-detail.html',
                    controller: 'OrganizationlocationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('organizationlocation');
                    $translatePartialLoader.addPart('status');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Organizationlocation', function($stateParams, Organizationlocation) {
                    return Organizationlocation.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('organizationlocation.new', {
            parent: 'organizationlocation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/organizationlocation/organizationlocation-dialog.html',
                    controller: 'OrganizationlocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isprimary: false,
                                status: null,
                                lastmodifiedby: null,
                                lastmodifieddatetime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('organizationlocation', null, { reload: true });
                }, function() {
                    $state.go('organizationlocation');
                });
            }]
        })
        .state('organizationlocation.edit', {
            parent: 'organizationlocation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/organizationlocation/organizationlocation-dialog.html',
                    controller: 'OrganizationlocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Organizationlocation', function(Organizationlocation) {
                            return Organizationlocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('organizationlocation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('organizationlocation.delete', {
            parent: 'organizationlocation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/organizationlocation/organizationlocation-delete-dialog.html',
                    controller: 'OrganizationlocationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Organizationlocation', function(Organizationlocation) {
                            return Organizationlocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('organizationlocation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
