(function() {

    'use strict';

   function focusonshow_directive() {
           return {
               restrict: 'A',
               link: function($scope, $element, $attr) {
                   if ($attr.ngShow){
                       $scope.$watch($attr.ngShow, function(newValue){
                           if(newValue){ // scroll up to the alert div only when the div appears
                               $('html, body').animate({ scrollTop: $('#alert').offset().top }, 'slow');
                           }
                       });
                   }
               }
           }
       }

    module.exports = focusonshow_directive;
})();