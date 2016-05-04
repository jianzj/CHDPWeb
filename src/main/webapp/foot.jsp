<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
            </div>
        </div>
    </div>

    <script src="<%=request.getContextPath()%>/js/jquery-1.12.3.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/common.js"></script>
    <script src="<%=request.getContextPath() %>/js/bootstrap-select.js"></script>
 	<script src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
	<script>
	$(document).ready(function() {
	    $('#hospital-datePicker-start')
	        .datepicker({
	            format: 'yyyy-mm-dd'
	        });
	    $('#hospital-datePicker-end')
        .datepicker({
            format: 'yyyy-mm-dd'
        });
	});
	</script>
</body>
</html>