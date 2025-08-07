
"use client";
import {SubmitHandler, useForm} from "react-hook-form";
import {valibotResolver} from "@hookform/resolvers/valibot";
import React from "react";
import crateScheme from "@/schema/user-example";
import {Button} from "@/components/ui/button";
import {DialogClose, DialogDescription, DialogFooter, DialogHeader, DialogTitle} from "@/components/ui/dialog";
import UserExampleApi from "@/api/auto/user-example";
import toast from "react-hot-toast";
import * as v from "valibot";
import {useTranslations} from "next-intl";
import {ValidatableTextArea} from "@/common/components/forms/ValidatableTextArea";
import {ValidatableInput} from "@/common/components/forms/ValidatableInput";
import {DialogCloseProps} from "@/common/lib/table/DataTableProperty";




export default function Page({callbackHandler}: DialogCloseProps) {
    const globalTranslate = useTranslations("GlobalForm");
    const errorTranslate = useTranslations("UserExample.ErrorMessage")
    const pageTranslate = useTranslations("UserExample")
    const validateTranslate = useTranslations("UserExample.validate")

    const FormSchema = crateScheme(validateTranslate);
    type FormData = v.InferOutput<typeof FormSchema>;


    const onSubmit: SubmitHandler<FormData> = (
        data: FormData,
        event: React.BaseSyntheticEvent | undefined
    ) => {
        UserExampleApi.save(data, errorTranslate).then(
            (res) => {
                callbackHandler();
                toast.success(globalTranslate("save")+globalTranslate("operation-success"));
            }
        ).catch(()=>{});
    };

    const {
        register,
        handleSubmit,
        control,
        formState: {
            errors,
            isSubmitted,
            touchedFields
        },
    } = useForm<FormData>({
        //相当于v.parse
        resolver: valibotResolver(
            FormSchema,
            //https://valibot.dev/guides/parse-data/
            {abortEarly: false, lang: "zh-CN"}
        ),
    });


    return (
            <form className={"h-[calc(100vh-80px)] flex flex-col"} onSubmit={handleSubmit(onSubmit)}>
                                   <DialogHeader>
                                       <DialogTitle>{globalTranslate("add")}</DialogTitle>
                                       <DialogDescription>
                                       </DialogDescription>
                                   </DialogHeader>
            <div className="min-h-0 flex-1 flex-col overflow-y-scroll">
                <ValidatableInput  {...register("id")}
                                  type={"hidden"}
                                  fieldPropertyName={"id"}/>
<ValidatableInput readonly={false}  {...register("userName")}
                                  type={"text"}
                                  isSubmitted={isSubmitted}
                                  pageTranslate={pageTranslate}
                                  validateTranslate={validateTranslate}
                                  errorMessage={errors.userName?.message}                                  fieldPropertyName={"userName"}/>
<ValidatableInput readonly={false}  {...register("chineseName")}
                                  type={"text"}
                                  isSubmitted={isSubmitted}
                                  pageTranslate={pageTranslate}
                                  validateTranslate={validateTranslate}
                                  errorMessage={errors.chineseName?.message}                                  fieldPropertyName={"chineseName"}/>
<ValidatableInput readonly={false}  {...register("birthday")}
                                  type={"text"}
                                  isSubmitted={isSubmitted}
                                  pageTranslate={pageTranslate}
                                  validateTranslate={validateTranslate}
                                  errorMessage={errors.birthday?.message}                                  fieldPropertyName={"birthday"}/>
<ValidatableInput readonly={false}  {...register("email")}
                                  type={"text"}
                                  isSubmitted={isSubmitted}
                                  pageTranslate={pageTranslate}
                                  validateTranslate={validateTranslate}
                                  errorMessage={errors.email?.message}                                  fieldPropertyName={"email"}/>
<ValidatableInput readonly={false}  {...register("mobile")}
                                  type={"text"}
                                  isSubmitted={isSubmitted}
                                  pageTranslate={pageTranslate}
                                  validateTranslate={validateTranslate}
                                  errorMessage={errors.mobile?.message}                                  fieldPropertyName={"mobile"}/>
<ValidatableInput readonly={false}  {...register("tel")}
                                  type={"text"}
                                  isSubmitted={isSubmitted}
                                  pageTranslate={pageTranslate}
                                  validateTranslate={validateTranslate}
                                  errorMessage={errors.tel?.message}                                  fieldPropertyName={"tel"}/>
<ValidatableInput readonly={false}  {...register("idCard")}
                                  type={"text"}
                                  isSubmitted={isSubmitted}
                                  pageTranslate={pageTranslate}
                                  validateTranslate={validateTranslate}
                                  errorMessage={errors.idCard?.message}                                  fieldPropertyName={"idCard"}/>
<ValidatableInput readonly={false}  {...register("gender")}
                                  type={"text"}
                                  isSubmitted={isSubmitted}
                                  pageTranslate={pageTranslate}
                                  validateTranslate={validateTranslate}
                                  errorMessage={errors.gender?.message}                                  fieldPropertyName={"gender"}/>
<ValidatableInput readonly={false}  {...register("age")}
                                  type={"text"}
                                  isSubmitted={isSubmitted}
                                  pageTranslate={pageTranslate}
                                  validateTranslate={validateTranslate}
                                  errorMessage={errors.age?.message}                                  fieldPropertyName={"age"}/>
            </div>
             <DialogFooter>
                            <DialogClose asChild>
                                <Button variant="outline">{globalTranslate("cancel")}</Button>
                            </DialogClose>
                            <Button type="submit">{globalTranslate("save")}</Button>
             </DialogFooter>
        </form>
    );
};