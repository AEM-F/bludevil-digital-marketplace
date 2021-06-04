export class ChatMessage {
  public constructor(public messageId: number,
                     public chatId: string,
                     public senderId: number,
                     public recipientId: number,
                     public senderName: string,
                     public recipientName: string,
                     public content: string,
                     public timestamp: Date,
                     public status: string) {
  }
}
