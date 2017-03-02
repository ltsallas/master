export class HelpdeskIssue {
    private portal: string;
    private topic: string;
    private memberState: string;
    private date: string;
    private time: string;
    private assignedTo: string;
    private status: string;
    private from: string;
    private issueSummary: string;
    private memberStateComments: string;
    private dgConnectComments: string;


    constructor(portal?: string, topic?: string, memberState?: string, date?: string, time?: string, assignedTo?: string, status?: string, from?: string, issueSummary?: string, memberStateComments?: string, dgConnectComments?: string) {
        this.portal = portal;
        this.topic = topic;
        this.memberState = memberState;
        this.date = date;
        this.time = time;
        this.assignedTo = assignedTo;
        this.status = status;
        this.from = from;
        this.issueSummary = issueSummary;
        this.memberStateComments = memberStateComments;
        this.dgConnectComments = dgConnectComments;
    }

    getPortal() {
        return this.portal;
    }

    getTopic() {
        return this.topic;
    }

    getMemberState() {
        return this.memberState;
    }

    getDate() {
        return this.date;
    }

    getTime() {
        return this.time;
    }

    getAssignedTo() {
        return this.assignedTo;
    }

    getStatus() {
        return this.status;
    }

    setStatus(status: string) {
        this.status = status;
    }

    getFrom() {
        return this.from;
    }

    getIssueSummary() {
        return this.issueSummary;
    }

    setIssueSummary(issueSummary: string) {
        this.issueSummary = issueSummary;
    }

    getMemberStateComments() {
        return this.memberStateComments;
    }

    setMemberStateComments(memberStateComments: string) {
        this.memberStateComments = memberStateComments;
    }

    getDgConnectComments() {
        return this.dgConnectComments;
    }

    setDgConnectComments(dgConnectComments: string) {
        this.dgConnectComments = dgConnectComments;
    }
}