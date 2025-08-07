
import {ColumnDef, filterFns} from "@tanstack/react-table";
import * as React from "react";
import {BasicData, ColumnOperationProps} from "@/common/lib/table/DataTableProperty";
import CheckBoxCell from "@/common/components/table/cell/check-box";
import NormalCell from "@/common/components/table/cell/normal";
import CheckboxHeader from "@/common/components/table/header/check-box";
import UnixTimestampCell from "@/common/components/table/cell/unix-timestamp";
import OperationCell from "@/common/components/table/cell/operation";
import ColumnFilter from "@/common/components/table/header/column-filter";
import PlainTextHeader from "@/common/components/table/header/plain-text";

export interface UserExample extends BasicData<UserExample> 
{
 id:number; 
userName:string; 
chineseName:string; 
birthday:string; 
email:string; 
mobile:string; 
tel:string; 
idCard:string; 
gender:string; 
age:number; 
createUserName:string; 
createUserId:number; 
modifiedUserId:number; 
modifiedUserName:string; 
gmtCreate:number; 
gmtModified:number; 
deleted:boolean; 
status:string; 

}
export const columns: ColumnDef<UserExample>[] = [
{
accessorKey: "id",
header: PlainTextHeader({columnTitle: "ID"} as ColumnOperationProps),
cell: NormalCell("id"),
enableHiding: true
},{
id: "select",
header: CheckboxHeader,
cell:CheckBoxCell,
enableHiding: false
},{
accessorKey: "userName",
header: PlainTextHeader({columnTitle: "用户名"} as ColumnOperationProps),
cell: NormalCell("userName"),
enableHiding: true
},{
accessorKey: "chineseName",
header: PlainTextHeader({columnTitle: "中文名"} as ColumnOperationProps),
cell: NormalCell("chineseName"),
enableHiding: true
},{
accessorKey: "birthday",
header: PlainTextHeader({columnTitle: "出生日期"} as ColumnOperationProps),
cell: NormalCell("birthday"),
enableHiding: true
},{
accessorKey: "email",
header: PlainTextHeader({columnTitle: "Email"} as ColumnOperationProps),
cell: NormalCell("email"),
enableHiding: true
},{
accessorKey: "mobile",
header: PlainTextHeader({columnTitle: "手机号"} as ColumnOperationProps),
cell: NormalCell("mobile"),
enableHiding: true
},{
accessorKey: "tel",
header: PlainTextHeader({columnTitle: "电话号码"} as ColumnOperationProps),
cell: NormalCell("tel"),
enableHiding: true
},{
accessorKey: "idCard",
header: PlainTextHeader({columnTitle: "身份证"} as ColumnOperationProps),
cell: NormalCell("idCard"),
enableHiding: true
},{
accessorKey: "gender",
header: PlainTextHeader({columnTitle: "性别"} as ColumnOperationProps),
cell: NormalCell("gender"),
enableHiding: true
},{
accessorKey: "age",
header: PlainTextHeader({columnTitle: "年龄"} as ColumnOperationProps),
cell: NormalCell("age"),
enableHiding: true
},{
accessorKey: "createUserName",
header: PlainTextHeader({columnTitle: "创建人"} as ColumnOperationProps),
cell: NormalCell("createUserName"),
enableHiding: true
},{
accessorKey: "createUserId",
header: PlainTextHeader({columnTitle: "创建人ID"} as ColumnOperationProps),
cell: NormalCell("createUserId"),
enableHiding: true
},{
accessorKey: "modifiedUserId",
header: PlainTextHeader({columnTitle: "更新人ID"} as ColumnOperationProps),
cell: NormalCell("modifiedUserId"),
enableHiding: true
},{
accessorKey: "modifiedUserName",
header: PlainTextHeader({columnTitle: "更新人"} as ColumnOperationProps),
cell: NormalCell("modifiedUserName"),
enableHiding: true
},{
accessorKey: "gmtCreate",
header: PlainTextHeader({columnTitle: "创建时间"} as ColumnOperationProps),
cell: UnixTimestampCell("gmtCreate"),
enableHiding: true
},{
accessorKey: "gmtModified",
header: PlainTextHeader({columnTitle: "更新时间"} as ColumnOperationProps),
cell: UnixTimestampCell("gmtModified"),
enableHiding: true
},{
accessorKey: "deleted",
header: PlainTextHeader({columnTitle: "是否删除"} as ColumnOperationProps),
cell: NormalCell("deleted"),
enableHiding: true
},{
accessorKey: "status",
header: PlainTextHeader({columnTitle: "状态"} as ColumnOperationProps),
cell: NormalCell("status"),
enableHiding: true
},{
id: "actions",
header: PlainTextHeader({columnTitle: "操作"} as ColumnOperationProps),
cell:"Actions",
enableHiding: false
},{
id: "filter-column",
header: ColumnFilter(),
cell:"",
enableHiding: false
}
];