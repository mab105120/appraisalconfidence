SET @user = '';
DELETE FROM ACTIVITY WHERE ID = @user;
DELETE FROM EVAL_ACTIVITY WHERE ID = @user;
DELETE FROM STATUS WHERE ID = @user;
DELETE FROM PERFORMANCE_REVIEW WHERE ID = @user;
DELETE FROM EVALUATION WHERE ID = @user;
DELETE FROM USER_CONFIDENCE WHERE ID = @user;
DELETE FROM USER_DEMOGRAPHIC WHERE ID = @user;
DELETE FROM USER_EXPERIENCE WHERE ID = @user;