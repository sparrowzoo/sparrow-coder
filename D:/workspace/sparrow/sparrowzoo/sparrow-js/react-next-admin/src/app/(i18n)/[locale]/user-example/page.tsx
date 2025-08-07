"use client";

import * as React from "react";
import {useEffect, useState} from "react";
import {columns, UserExample} from "@/components/user-example/columns";
import {DataTable} from "@/common/components/table/data-table";
import Search from "@/components/user-example/search";
import Operation from "@/components/user-example/operation";
import EditPage from "@/components/user-example/edit";
import ThreeDotLoading from "@/common/components/ThreeDotLoading";
import UserExampleApi from "@/api/auto/user-example";
import {useTranslations} from "next-intl";
import toast from "react-hot-toast";
import Result, {PagerResult} from "@/common/lib/protocol/Result";




export default function Page() {
    const errorTranslate = useTranslations("UserExample.ErrorMessage");
    const globalTranslate = useTranslations("GlobalForm");
    const [dataState, setDataState] = useState<Result<PagerResult<UserExample>> | undefined>();
    const pagination = {pageIndex: 0, pageSize: 10};

    const init = () => {
                UserExampleApi.search({...pagination}, errorTranslate).then(
                    (res) => {
                        setDataState(res)
                    }
                ).catch(() => {
                });
            };
            useEffect(() => {
                init();
            }, []);


      const deleteHandler= (id: number) => {
            UserExampleApi.delete(id, errorTranslate).then(()=>{
                toast.success(globalTranslate("delete")+globalTranslate("operation-success"));
            }).catch(()=>{});
        }

    if (!dataState) {
        return <ThreeDotLoading/>
    }
    return (
        <div className="w-full">
            <DataTable<UserExample>
                SearchComponent={Search}
                OperationComponent={Operation}
                tableName={"UserExample"}
                primary={"id"}
                i18n={false}
                result={dataState}
                columns={columns}
                setData={setDataState}
                EditComponent={EditPage}
                deleteHandler={deleteHandler}
                initHandler={init}
                defaultPager={pagination}

                defaultRowOperationComponents={[]}
            ></DataTable>
        </div>
    );
}