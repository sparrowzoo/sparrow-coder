
import Fetcher from "@/common/lib/Fetcher";
import {IDENTITY} from "@/common/lib/protocol/Identity";

export default class UserExampleApi {
    public static search(
        query: any,
        translator: (key: string) => string
    ): Promise<any> {
        const body = JSON.stringify(query);
        return Fetcher.post("/user/example/search.json", body, translator);
    }

    public static  save(
        params: any,
        translator: (key: string) => string
    ): Promise<any> {
        const body = JSON.stringify(params);
        return Fetcher.post("/user/example/save.json", body, translator);
    }

    public static  batchDelete(
        params: IDENTITY[],
        translator: (key: string) => string
    ): Promise<any> {
        const body = JSON.stringify(params);
        return Fetcher.post("/user/example/delete.json", body, translator);
    }

       public static  delete(
            id: IDENTITY,
            translator: (key: string) => string
        ): Promise<any> {
            const body = JSON.stringify([id]);
            return Fetcher.post("/user/example/delete.json", body, translator);
        }


    public static  disable(
        params: IDENTITY[],
        translator: (key: string) => string
    ): Promise<any> {
        const body = JSON.stringify(params);
        return Fetcher.post("/user/example/disable.json", body, translator);
    }

    public static  enable(
        params: IDENTITY[],
        translator: (key: string) => string
    ): Promise<any> {
        const body = JSON.stringify(params);
        return Fetcher.post("/user/example/enable.json", body, translator);
    }
}