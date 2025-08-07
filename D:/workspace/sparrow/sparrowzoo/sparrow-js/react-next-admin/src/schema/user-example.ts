
import * as v from "valibot";
function createSchema(translate:(key:string)=>string) {
    const InnerFormSchema = v.object({
        id:
v.union([v.literal(""),v.pipe(
 v.string(),
v.check((val) => {return /^\d+$/.test(val);},"请输入正确的数字"),
v.transform((input): number | string => {return parseInt(input,10);}))
], (issue) => {        if (issue.issues) {
            return issue.issues[issue.issues.length - 1].message;
        }
        return "";
    })
,userName:
v.pipe(
 v.string(),
v.nonEmpty("不允许为空"))

,chineseName:
v.pipe(
 v.string(),
v.nonEmpty("不允许为空"))

,birthday:
v.pipe(
 v.string(),
v.nonEmpty("不允许为空"))

,email:
v.pipe(
 v.string(),
v.nonEmpty("不允许为空"))

,mobile:
v.pipe(
 v.string(),
v.nonEmpty("不允许为空"))

,tel:
v.pipe(
 v.string(),
v.nonEmpty("不允许为空"))

,idCard:
v.pipe(
 v.string(),
v.nonEmpty("不允许为空"))

,gender:
v.pipe(
 v.string(),
v.nonEmpty("不允许为空"))

,age:
v.pipe(
 v.string(),
v.nonEmpty("不允许为空"),
v.check((val) => {return /^\d+$/.test(val);},"请输入正确的数字"),
v.transform((input): number | string => {return parseInt(input,10);}))


    });
    //扩展提示
    const FormSchema = InnerFormSchema;
    //type FormData = v.InferOutput<typeof FormSchema>;
    return FormSchema
}
export default createSchema;