department.txt	园区全局一个文件
	RoomId: 三位十进制数，在一层楼中统一编号。一个科室可能由多个房间组成，因此可以有多个roomid
	MapId:楼号楼层号，例如0101表示1号楼1层。在一个园区中统一编号。0191表示1号楼地下1层
doctor.txt	园区全局一个文件
	去掉了workday，不需要保留门诊时间。
	id在医院中统一编号，而不是在科室中统一编号。
map.txt		园区全局一个文件

room.txt	园区全局一个文件
	id是园区全局的room id，由mapid和roomid拼接而成,去掉了mapId
	deparment改成了DeparmentId
	vertexId即为vertex.txt中的id
vertex.txt	园区全局一个文件
	id是园区全局的id,由mapid和每一层中的vertexId拼接而成,其中vertexId为4位10进制数
	去掉了mapid



------------------------------------------
修改内容：

1.把mapid分离出来
2.一个department可以对应多个房间，在department_has_room.txt文件中，majorroomId表示这个department的主房间，也就是代表性房间。
3.在roomarea.txt中没有使用vertexId列表，而采用经纬度列表，是因为房间边界的点并没有出现在vertex.txt文件中，vertex.txt中出现的只是所有路径经过的关键点。如果必要，可以单独建立一个边界点的文件。
4.room中还是增加了departmentId，否则还得到department_has_room文件中去找，如果确定不会影响性能，可以删除。

