angular
	.module('roomReservation')
	.factory('Access',['$http',function($http){
	    var vm = this;
	
	    vm.user =undefined;
	    vm.visible = false;
	    
	    var Access = {
	
	        user : function(user){
	          vm.user = user;
	        },
	        
	        getUser: function(){
	        	return vm.user;
	        },
	        
	        showNav: function(visible){
	        	vm.visible = visible;
	        	//console.log("toggle navigation: "+visible);
	        },
	        
	        canShowNav: function(){
	        	return vm.visible;
	        },
	        
	        isAuthenticated : function(){
	          var val = (vm.user == null || vm.user == undefined)? false: true;
	        //  console.log(vm.user);
	          return val;
	        },
	     
	        
	        getClaimsFromToken: function(token){
	        	return $http({
	    			method: 'POST',
	    			url: '/RoomReservation/user-claims',
	    		    headers: { 'Authorization': 'Bearer '+token }
	    		})
	        }
	    };
	
	    return Access;
	  }]);