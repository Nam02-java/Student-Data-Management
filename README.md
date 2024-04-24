# Student-Data-Management
High school student data management software

Tiếng việt :

- Phần mềm này phục vụ việc quản lý dữ liệu của một lớp học

- Sẽ có hai dạng user trong phần mềm
  + Administrator (ROLE ADMIN , ROLE USER) , tương đương là giáo viên chủ nhiệm (GVCN)
  + User (ROLE USER) , tương đương là phụ huynh học sinh (PHHS)
    
- Trong đó Admin có quyền CRUD toàn bộ dữ liệu học sinh
- Admin chỉ có thể Create , Read , Delete dữ liệu User , tuy nhiên Create đối với User chỉ có ủy quyền ROLE ADMIN sang một User do Admin đang chỉ định , ngay sau khi xác thực mật khẩu Admin sẽ mất ROLE ADMIN ngay lập tức và phải đăng nhập lại , còn ROLE ADMIN đã được ủy sang cho User đã được chỉ định ( áp dụng trong những trường hợp lớp học thay thế GVCN mà theo thực tế một lớp học chỉ có duy nhất một GVCN )


- User có thể Read , Update
- Trong đó việc User Read dữ liệu học sinh ( con của họ ) được thể hiện trên giao diện phần mềm thông qua số điện thoại , nếu số điện của bố mẹ mà học sinh đã đăng ký với GVCN trùng khớp với số điện thoại của User hiện tại đang đăng nhập thì sẽ hiển thị dữ liệu học sinh đó trên giao diện phần mềm
- User chỉ có thể Read , Update dữ liệu của bản thân họ

---------------------------------------------------------------------

English :

- This software supports a classroom's data management.

- The software will cater to two different user types.
  + Administrator (ROLE ADMIN, ROLE USER), which is the homeroom instructor's equivalent (teacher)
  + User (ROLE USER), which is the same as a parent student (PHHS)

- All student data may be CRUD by the administrator
- Only Admin has the ability to Create, Read, and Delete User data; however, Create for User only grants ROLE ADMIN authorization to a User that Admin designates, following password authentication. The selected User has been assigned the ROLE ADMIN, which will be instantly lost by the Admin and they will need to log in again (applied in circumstances where the class substitutes the teacher in practice). There is actually just one homeroom instructor in a classroom


- The user is able to read and update
- When the student's phone number matches the parent's phone number that the student registered with the teacher, the User can read the student's data (their child) via the phone number on the software interface. That student data will appear on the software interface when you log in right now.
- Users can only Read and Update their own data
