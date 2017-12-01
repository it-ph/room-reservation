angular
	.module('roomReservation')
	.controller('RoomController',['$rootScope', '$scope', '$state','RoomDataOp',
		function($rootScope, $scope, $state,RoomDataOp){
		
	
		$scope.itemList =[];
		$scope.filteredList =[];
		$scope.item ={};
		$scope.newItem = {};
		$scope.selectedItem ={};
		$scope.roleList =[];
		$scope.totalItems = null;
		$scope.currentPage = 1;
		$scope.maxSize = 5;
		$scope.numPerPage = 10;
		$scope.search_keyword ='';
		$scope.error ='';
		$scope.success_msg =''
		loadData();
		
		$scope.setPage = function (pageNo) {
			$scope.currentPage = pageNo;
		};
	
		$scope.pageChanged = function() {
	
			 var begin = (($scope.currentPage - 1) * $scope.numPerPage);
			 var end = begin + $scope.numPerPage;
			 $scope.filteredList = $scope.itemList.slice(begin, end);
	
		};
		
		function loadData(){
			RoomDataOp
			 .getRooms()
			 .then(function(response){
				 console.log(response);
				 
				$scope.itemList = response.data;
				
				$scope.totalItems = $scope.itemList.length;
				var begin = (($scope.currentPage - 1) * $scope.numPerPage);
				var end = begin + $scope.numPerPage;
				$scope.filteredList = $scope.itemList.slice(begin, end);	
			 })
			 .catch(function(error){
				 console.log(error);
			 })
		}
		
		function refresh(){
			$scope.totalItems = $scope.itemList.length;
			var begin = (($scope.currentPage - 1) * $scope.numPerPage);
			var end = begin + $scope.numPerPage;
			$scope.filteredList = $scope.itemList.slice(begin, end);		
		}
	}]); 