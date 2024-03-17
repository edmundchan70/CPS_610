--demo command 
Create TYPE St_Rec as object (
St_Name Varchar(10),
St_Id Number(7),
St_Email varchar(50));
/

Create Table st_table of St_Rec;

--1 Create a new type named “depart_type” with this schema
Create TYPE depart_type as object(
	Name varchar(50),
	Faculty varchar(30),
	Building varchar(30),
	Phone varchar(10)
)
--2 Create table using type 
Create table Professor (
	Name varchar(50),
	Emp_id int ,
	email varchar(100),
	depart_type  depart_type,
    Primary Key(Emp_id)
)
--3 INsert  data 
	-- Inserting Professor 1
	INSERT INTO Professor (Name, Emp_id, email, depart_type)
	VALUES ('John', 1001, 'john@example.com', depart_type('Computer Science', 'Engineering', 'Main_Building', '1234567890'));
	
	-- Inserting Professor 2
	INSERT INTO Professor (Name, Emp_id, email, depart_type)
	VALUES ('Alice', 1002, 'alice@example.com', depart_type('Mathematics', 'Science', 'Science_Building', '2345678901'));
	
	-- Inserting Professor 3
	INSERT INTO Professor (Name, Emp_id, email, depart_type)
	VALUES ('Michael', 1003, 'michael@example.com', depart_type('Physics', 'Science', 'Science_Building', '3456789012'));
	
	-- Inserting Professor 4
	INSERT INTO Professor (Name, Emp_id, email, depart_type)
	VALUES ('Sarah', 1004, 'sarah@example.com', depart_type('Biology', 'Science', 'Science_Building', '4567890123'));
	
	-- Inserting Professor 5
	INSERT INTO Professor (Name, Emp_id, email, depart_type)
	VALUES ('David', 1005, 'david@example.com', depart_type('Chemistry', 'Science', 'Science_Building', '5678901234'));

--4 Display all data for prfessor 
SELECT * FROM PROFESSOR ;
	
-- 5 add new column INCOME  for simplicity choosen integer 
ALTER TABLE professor 
ADD INCOME INTEGER;  -- Add the INCOME column to the Professor table

-- Update the income values for each professor
UPDATE Professor
SET INCOME = 
    CASE Name
        WHEN 'John' THEN 60000
        WHEN 'Alice' THEN 65000
        WHEN 'Michael' THEN 70000
        WHEN 'Sarah' THEN 62000
        WHEN 'David' THEN 68000
    END;
--6  write for loop, display tax and income for professor 
Begin
	for i in (SELECT * FROM PROFESSOR) loop
	
			Begin
                dbms_output.put_line('Professor ' || i.Name || ', Tax: ' || i.INCOME *0.3);
            END;
		end loop;
END;
--7 
CREATE PROCEDURE Q7 iS  
Begin
	for i in (SELECT * FROM PROFESSOR where INCOME < 40000) loop
	
			Begin
                dbms_output.put_line('Professor ' || i.Name || ', INCOME: ' || i.INCOME);
            END;
		end loop;
END;

--8
Create procedure findAverageIncome as  
Begin 
            numOfProfs Integer;
            totalIncome Integer;
    begin 
       SELECT count(*) INTO numOfProfs FROM professor;
       totalIncome :=0 ;
        for i in (SELECT  *  FROM professor) LOOP 
            totalIncome := totalIncome + i.INCOME;
        end LOOP;
            DBMS_OUTPUT.PUT_LINE('TotalIncome:' || totalIncome);
            DBMS_OUTPUT.PUT_LINE('Num of Profs: ' ||numofprofs);
            DBMS_OUTPUT.PUT_LINE('Average' || ROUND(totalIncome / numofprofs, 2));
    end;
END;

