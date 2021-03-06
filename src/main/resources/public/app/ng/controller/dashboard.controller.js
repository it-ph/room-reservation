angular
	.module('roomReservation')
	.controller('DashboardController',['$scope','$filter','ReservationDataOp',function($scope,$filter,ReservationDataOp){

		$scope.events =[];
		$scope.filteredEvents = [];
		
		$scope.rooms =[];
		$scope.roomSelection =[];
		
		$scope.uiConfig ={};
		
		  $scope.schedule ={
				  title:undefined,
				  events:[],
				  room:{},
				  createdBy:{}
		  };
		  
		  $scope.weekDaySelections = [
			  { num: 0, text: 'Su' , selected: false },
			  { num: 1, text: 'Mo' , selected: false },
			  { num: 2, text: 'Tu' , selected: false },
			  { num: 3, text: 'We' , selected: false },
			  { num: 4, text: 'Th' , selected: false },
			  { num: 5, text: 'Fr' , selected: false },
			  { num: 6, text: 'Sa' , selected: false },
		  ];
		  
		  $scope.selectDay = function(weekday){
			  weekday.selected = !weekday.selected;
		  };
		  
		  $scope.selectedDays =[];
		  
		  $scope.isRepeat = false;
		  
		  $scope.schedStartDate = new Date();			  
		  $scope.schedEndDate = new Date();
		  $scope.repeatEnd = new Date();
		  
		
		
		ReservationDataOp
			.getEvents()
			.then(function(response){
				
				evt = response.data;
				
				evt.forEach(function(obj){
					
	    			$scope.events.push({
	    				id: (obj.repeatId == 0) ? undefined: obj.repeatId,
	    				title: obj.title+' - '+ obj.room.room,
	    				start: moment.utc(obj.start).format(),
	    				end: moment.utc(obj.end).format(),
	    				color: obj.room.color_code,
	    				room_event: obj,
	    			});
	    			
				
	    		});
				
			})
			.catch(function(error){
				console.log(error);
			})
		
	
		ReservationDataOp
			.getRooms()
			.then(function(response){
				$scope.rooms = response.data;
				
				$scope.rooms.splice(0,0,{id:0,room:'ALL'});
			}).
			catch(function(error){
				
			});

		
	    $scope.uiConfig.calendar = getCalendar($scope.events);
	    
		$scope.tabSelect = function(tab){
		
			events =(tab==0) ? $scope.events : $filter('filter')($scope.events,{room_event:{room:{id:tab}}},true);
			
			$scope.uiConfig.calendar = getCalendar(events);
		}
		
	

	   $scope.conflictEvents =[];
	   $scope.numConflicts = 0;
	   
	   $scope.loadRooms = function(){
		   
			ReservationDataOp
			.getRooms()
			.then(function(response){
				$scope.roomSelections = response.data;					
			}).
			catch(function(error){
				
			});
	   }
	   
	
	
	   function getCalendar(events){
	      
		//return a calendar object
		calendar = {
			        height: 500,
			        editable: true,
			        nextDayThreshold: "00:00:00",
			        timezone: false,
			        snapDuration: "00:01:00",
			        allDaySlot: false,
			        header:{
			        	left: 'prev,next today',
						center: 'title',
						right: 'month,agendaWeek,agendaDay,listMonth,listWeek,listDay'
			        },
			        views: {
			        	listMonth: { buttonText: 'list month' },
			        	listWeek: { buttonText: 'list week' },
			        	listDay: { buttonText: 'list day' },
			        },
			        eventClick: onEventClick,
			        eventDrop: onEventDrop,
			        eventResize: onResize,
			        events: events
		      };
			return calendar;
		}
	   
		function onEventClick(date, jsEvent, view){
			  reservation ={
		        		event: date.id,
		        		title: date.room_event.title,
		        		room: date.room_event.room.room,
		        		start: $filter('date')(date.start.format('MMM. DD, YYYY hh:mm a')),//moment.js format
		        		end: $filter('date')(date.end.format('MMM. DD, YYYY hh:mm a')),
		        	}
			  console.log(date);
			  
			  $scope.selectedEvent = reservation;
			  
			  $('#view-event').modal('show');
		}
		 
		 
		function onEventDrop(date, delta, revertFunc, jsEvent, ui, view){
		       	       
		       s = moment(date.start);
		       e = moment(date.end);
		       
		       $scope.conflictEvents =[];
		       
		       events = [];
		       droppedEvents =[];
		       
		       
		       if(date.id ==undefined){ //if no repeat event was dropped
		    	   
		    	   
		    	   events.push({
		    		   
		        		id: date.room_event.id,
		        		title: date.room_event.title,
		        		start: new Date(s),
		        		end: new Date(e),
		        		room: date.room_event.room,
		        		createdBy: date.room_event.createdBy
		        	});
		    	   
		    	   droppedEvents = events;
		    	   
		       }else{ //if repeating events was dropped
		    	   
		    	   events = view.calendar.clientEvents(date.id);
		    	   
		    	   events.forEach(function(o){
		    		   
		    		   st = moment(o.start);
		    		   en = moment(o.end);
		    		   
		    		   droppedEvents.push({
		    				id: o.room_event.id,
		    				repeatId: o.room_event.repeatId,
			        		title: date.room_event.title,
			        		start: new Date(st),//moment.js format
			        		end: new Date(en),
			        		room: o.room_event.room,
			        		createdBy: o.room_event.createdBy
		    			});
		    		   
		    	   });
		    	   
		    	   
		    	   
		       }
		       console.log(droppedEvents);
		       var revert = false;
		       
	    	   var numConflict = 0;
		       
	    	   ReservationDataOp
			    .getConflicts(droppedEvents)
			    .then(function(response){
			       		
		       		
		       		evt = response.data;
		       		
	       			evt.forEach(function(obj){
						
						ev = {
								id: (obj.repeatId == 0) ? undefined: obj.repeatId,
			    				title: obj.title+' - '+ obj.room.room,
			    				start: new Date(moment(obj.start).format('YYYY-MM-DD HH:mm:ss')),
			    				end: new Date(moment(obj.end).format('YYYY-MM-DD HH:mm:ss')),
			    				color: obj.room.color_code,
			    				room_event: obj,
				    				
				    		};
		    			$scope.conflictEvents.push(ev);

		    		});
		       		
	       			if(response.data.length> 0){
	       				revertFunc();
	       				console.log(evt);
	       			}else{
	       				
	       			  ReservationDataOp
	       			   .updateEvent(droppedEvents)
	       			   .then(function(response){
	       				  //console.log(response); 
	       			   })
	       			   .catch(function (error){
	       				   console.log(error);
	       			   });
	       			}
					
		       		
		       		
		       	})
		       	.catch(function(error){
		       		console.log(error);
		       	});
	    	   
	    	   
		 }
		 
		 function onResize(date, delta, revertFunc, jsEvent, ui, view ){

			   s = moment(date.start);
		       e = moment(date.end);
		       
		       $scope.conflictEvents =[];
		       
		       events = [];
		       droppedEvents =[];
		       
		       console.log(s);
		       console.log(e);
		       
			 if(date.id ==undefined){ //if no repeat event was dropped
		    	   
		    	   
		    	   events.push({
		    		   
		        		id: date.room_event.id,
		        		title: date.room_event.title,
		        		start: new Date(s),
		        		end: new Date(e),
		        		room: date.room_event.room,
		        		createdBy: date.room_event.createdBy
		        	});
		    	   
		    	   droppedEvents = events;
		    	   console.log('single');
		       }else{ //if repeating events was dropped
		    	   
		    	   events = view.calendar.clientEvents(date.id);
		    	   
		    	   events.forEach(function(o){
		    		   
		    		   st = moment(o.start);
		    		   en = moment(o.end);
		    		   
		    		   droppedEvents.push({
		    				id: o.room_event.id,
		    				repeatId: o.room_event.repeatId,
			        		title: date.room_event.title,
			        		start: new Date(st),//moment.js format
			        		end: new Date(en),
			        		room: o.room_event.room,
			        		createdBy: o.room_event.createdBy
		    			});
		    		   
		    	   });
		    	   
		    	   
		    	   
		       }
		       console.log(droppedEvents);
		       var revert = false;
		       
	    	   var numConflict = 0;
		       
	    	   ReservationDataOp
			    .getConflicts(droppedEvents)
			    .then(function(response){
			       		
		       		
		       		evt = response.data;
		       		
	       			evt.forEach(function(obj){
						
						ev = {
								id: (obj.repeatId == 0) ? undefined: obj.repeatId,
			    				title: obj.title+' - '+ obj.room.room,
			    				start: new Date(moment(obj.start).format('YYYY-MM-DD HH:mm:ss')),
			    				end: new Date(moment(obj.end).format('YYYY-MM-DD HH:mm:ss')),
			    				color: obj.room.color_code,
			    				room_event: obj,
				    				
				    		};
		    			$scope.conflictEvents.push(ev);

		    		});
		       		
	       			if(response.data.length> 0){
	       				revertFunc();
	       				console.log(evt);
	       			}else{
	       				
	       			  ReservationDataOp
	       			   .updateEvent(droppedEvents)
	       			   .then(function(response){
	       				  //console.log(response); 
	       			   })
	       			   .catch(function (error){
	       				   console.log(error);
	       			   });
	       			}
					
		       		
		       		
		       	})
		       	.catch(function(error){
		       		console.log(error);
		       	});
	    	   
		 }
		 
		 //date picker
		 $scope.startDate = new Date();
		 $scope.endDate = new Date();
		 
		 
		  $scope.dateTimeNow = function() {
			    $scope.date = new Date();
			  };
			  $scope.dateTimeNow();
			  
			  $scope.toggleMinDate = function() {
			    var minDate = new Date();
			    var maxDate = new Date();
			    // set to yesterday
			    minDate.setDate(minDate.getDate() - 1);
			    maxDate.setDate(maxDate.getDate() + 3);
			    $scope.dateOptions.minDate = $scope.dateOptions.minDate ? null : minDate;
//			    $scope.dateOptions.maxDate = $scope.dateOptions.maxDate ? null : maxDate;
			  };
			   
			  $scope.dateOptions = {
			    showWeeks: false,
			    startingDay: 0
			  };
			  
			  $scope.toggleMinDate();
			  
			  // Disable weekend selection
			  $scope.disabled = function(calendarDate, mode) {
			    return mode === 'day' && ( calendarDate.getDay() === 0 || calendarDate.getDay() === 6 );
			  };
			  
			  $scope.open = function($event,opened) {
			    $event.preventDefault();
			    $event.stopPropagation();
			    $scope.dateOpened = true;
			  };
			  
			  $scope.dateOpened = false;
			  $scope.hourStep = 1;
			  $scope.format = "MMM. dd, yyyy";
			  $scope.minuteStep = 1;
			  // add min-time="minTime" to datetimepicker to use this value 
			  $scope.minTime = new Date(0, 0, 0, Math.max(1, $scope.date.getHours() - 2), 0, 0, 0);

			  $scope.timeOptions = {
			    hourStep: [1, 2, 3],
			    minuteStep: [1, 5, 10, 15, 25, 30]
			  };

			  $scope.showMeridian = true;
			  $scope.timeToggleMode = function() {
			    $scope.showMeridian = !$scope.showMeridian;
			  };
			  
			  $scope.$watch("date", function(date) {
			    // read date value
			  }, true);
			  
			  $scope.resetHours = function() {
			    $scope.date.setHours(1);
			  };
			  
			  $scope.selectedRoom ={};
			  $scope.eventTitle ='';
			  
			  
			  
			  //timepicker
			  $scope.changed = function () {
				    console.log('time changed');
			  };
			  
			
			  
			   function isValidEvent(){
				   
			   }
			   
		  
			  $scope.addEvent = function(){
				  
				  
				 e = moment($scope.endDate);
				 s = moment($scope.startDate);
				 until = moment($scope.repeatEnd);
				 
				 if(s.diff(e) < 0 )  {
					 
					 events =[];
					 
//					 events.push({
//						title: $scope.eventTitle,
//						start: new Date(s.format('YYYY-MM-DD HH:mm:ss')),
//						end: new Date(e.format('YYYY-MM-DD HH:mm:ss')),
//						room: $scope.selectedRoom,
//						createdBy: {id:1}
//					 });
					 
					 mondays = [];
					 
					 daysOfWeek =$scope.weekDaySelections;//[];
					
					
					 temp  = s.clone();
					 
					// mondays.push(temp.format('YYYY-MM-DD HH:mm:ss'));
					 
					 
					 while(temp.isBefore(until)){
						 
						

						 temp = temp.add(1,'days');	 
						 day = temp.weekday();
						 
						
						 
						// console.log(temp.weekday());
						 
						 if($scope.weekDaySelections[day].selected){
							 mondays.push(temp.format('MMM-DD-YYYY'));
							 
							 tempStart = temp.clone();
							 
							 tempEnd = temp.clone();
							 
							 tempEnd.set({ h: e.hour(), m: e.minutes()});
							 events.push({
									title: $scope.eventTitle,
									start: new Date(tempStart.format('YYYY-MM-DD HH:mm:ss')),
									end: new Date(tempEnd.format('YYYY-MM-DD HH:mm:ss')),
									room: $scope.selectedRoom,
									createdBy: {id:1}
								 });
						 }
						 
						 
						 
						 //mondays.push(temp.format('MMM-DD-YYYY'));
					 }
					 
					 
					 console.log(mondays);
//					console.log(events);
//					 
					ReservationDataOp
						.addEvent(events)
						.then(function(response){
							console.log(response);
						})
						.catch(function(error){
							console.log(error);
						})
				
				 }
			  }
			  
			
		 
	}]);