import Head from "next/head";
import Card from "./card";

export default function SingleCardPage(props) {
    return <>
        <Head>
            <title>{props.title}</title>
        </Head>
        <div className="page-heading">
            <div className="page-title">
                <div className="row">
                    <h3>{props.headertext}</h3>
                </div>
            </div>
        </div>
        <div className="page-content">
            <section className="section">
                <div className="row match-height">
                    <div className="col-md-6 col-12">
                        {
                            'cardtitle' in props ?
                                <Card title={props.cardtitle}>
                                    {props.children}
                                </Card> :
                                <Card>
                                    {props.children}
                                </Card>
                        }
                    </div>
                </div>
            </section>
        </div>
    </>
}