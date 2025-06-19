import * as v from "valibot";

export const InnerFormSchema = v.object({
    age: v.union([
        v.literal("", ""),
        v.pipe(
            v.string(),
            v.check((val) => {
                debugger;
                return /^\d+$/.test(val);
            }, "必须是数字"),
            v.transform((age) => {
                return parseInt(age);
            }),
            v.minValue(5, (issue) => {
                return `年龄必须大于等于5岁 ${issue.received}`
            }),
            v.maxValue(100, "年龄必须小于等于100岁")
        )
    ], (issue) => {
        if (issue.issues) {
            return issue.issues[issue.issues.length - 1].message;
        }
        return "";
    })
});
export const FormSchema = InnerFormSchema;
export type FormData = v.InferOutput<typeof FormSchema>;

