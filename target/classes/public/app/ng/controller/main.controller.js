angular
	.module('roomReservation')
	.controller('MainController',['$rootScope', '$scope', '$state','Access',
		function($rootScope, $scope, $state,Access){
		
		$scope.isAuthenticated = function(){
			return Access.isAuthenticated();
		}
		
	}]); 