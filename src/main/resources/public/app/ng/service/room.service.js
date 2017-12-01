angular
	.module("roomReservation")
	.factory('RoomDataOp',['$http',function($http){
		
		var RoomDataOp ={};
		
		RoomDataOp
			.getRooms = function(){
				
				return $http({
					url: '/RoomReservation/rooms',
					method: 'GET',
				});
			}
		
		RoomDataOp
			.addRoom = function(room){
				
				return $http({
					url: '/RoomReservation/rooms',
					method: 'POST',
					data: room,
					headers: { 'Content-Type': 'application/json; charset=UTF-8' },
				});
			}
		
		RoomDataOp
			.addRoom = function(room){
				
				return $http({
					url: '/RoomReservation/rooms',
					method: 'PUT',
					data: room,
					headers: { 'Content-Type': 'application/json; charset=UTF-8' },
				});
			}
		return RoomDataOp;
		
	}]);