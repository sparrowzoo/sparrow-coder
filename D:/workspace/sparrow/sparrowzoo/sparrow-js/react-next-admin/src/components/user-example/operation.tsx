import * as React from "react";
import {UserExample} from "@/components/user-example/columns";
import {TableOperationProps,MyTableMeta} from "@/common/lib/table/DataTableProperty";
import {Button} from "@/components/ui/button";
import {Dialog, DialogContent, DialogTrigger} from "@/components/ui/dialog";
import AddPage from "@/components/user-example/add";
import UserExampleApi from "@/api/auto/user-example";
import toast from "react-hot-toast";
import TableUtils from "@/common/lib/table/TableUtils";
import {useTranslations} from "next-intl";



export default function Operation({table}: TableOperationProps<UserExample>) {
    const globalTranslate = useTranslations("GlobalForm");
    const errorTranslate = useTranslations("UserExample.ErrorMessage")
    const [open, setOpen] = React.useState(false);
    const meta=table.options.meta as MyTableMeta<UserExample>;

    const initHandler=meta.initHandler;
    const setData=meta.setData;
    const result=meta.result;

    const callbackHandler = () => {
        setOpen(false);
        initHandler();
    }

    return (<div className="flex justify-between gap-4">
            <Dialog onOpenChange={setOpen} open={open}>
                <DialogTrigger asChild>
                    <Button onClick={() => setOpen(true)}  variant="outline">{globalTranslate("add")}</Button>
                </DialogTrigger>
                <DialogContent className="w-[800px] sm:max-w-[625px]">
                     <AddPage callbackHandler={callbackHandler}/>
                </DialogContent>
            </Dialog>
            <Button onClick={() => {
                const selectedIds = TableUtils.getSelectedIds(table);
                if (selectedIds.length === 0) {
                                    toast(globalTranslate("no-record-checked"));
                                    return;
                }
                UserExampleApi.batchDelete(selectedIds, errorTranslate).then(
                    (res) => {
                                            const datas= TableUtils.removeRowByPrimary(selectedIds,table);
                                            result.data.list=datas;
                                            result.data.recordTotal-=selectedIds.length;
                                            setData(TableUtils.cloneResult(result));
                        toast.success(globalTranslate("delete")+globalTranslate("operation-success"));
                    }
                )
            }} variant="outline">{globalTranslate("delete")}</Button>

            <Button onClick={() => {
                            const selectedIds = TableUtils.getSelectedIds(table);
                            if (selectedIds.length === 0) {
                                                toast(globalTranslate("no-record-checked"));
                                                return;
                            }
                            UserExampleApi.enable(selectedIds, errorTranslate).then(
                                (res) => {
                                   const datas= TableUtils.batchEnable(selectedIds,table,"status");
                                   result.data.list=datas;
                                   result.data.recordTotal-=selectedIds.length;
                                   setData(TableUtils.cloneResult(result));
                                   toast.success(globalTranslate("enable")+globalTranslate("operation-success"));
                                }
                            )
                        }} variant="outline">{globalTranslate("enable")}</Button>


                        <Button onClick={() => {
                                        const selectedIds = TableUtils.getSelectedIds(table);
                                        if (selectedIds.length === 0) {
                                                            toast(globalTranslate("no-record-checked"));
                                                            return;
                                        }
                                        UserExampleApi.disable(selectedIds, errorTranslate).then(
                                            (res) => {
                                                 const datas= TableUtils.batchDisable(selectedIds,table,"status");
                                                 result.data.list=datas;
                                                 result.data.recordTotal-=selectedIds.length;
                                                 setData(TableUtils.cloneResult(result));
                                                toast.success(globalTranslate("disable")+globalTranslate("operation-success"));
                                            }
                                        )
                                    }} variant="outline">{globalTranslate("disable")}</Button>
        </div>
    );
}